# MQ 文件拓扑

## 文件清单（仅 7 个文件涉及 MQ）

```
pom.xml                          ← spring-boot-starter-amqp 依赖
application-local.yml            ← spring.rabbitmq.* 连接配置

mq/RabbitMQConfig.java           ← 声明 Queue/Exchange/Binding + JSON 转换器
event/TagChangedEvent.java       ← Spring 事件，携带 userId
listener/TagChangedListener.java ← @EventListener，去重 + 发 MQ
mq/RecommendCacheConsumer.java   ← @RabbitListener，接收 MQ + 委托刷新

service/impl/AIRecommendServiceImpl.java ← refreshCandidateCache() 查库重建缓存
```

## 运行时链路

```
PUT /api/tag/{userId}
  │
  ▼
TagServiceImpl.updateTags()
  ├─ DELETE + INSERT user_tag
  └─ publishEvent(new TagChangedEvent(userId))
       │
       ▼  Spring 事件总线
TagChangedListener.onTagChanged()
  ├─ SETNX 去重（Redis, 30s）
  ├─ convertAndSend ──────────────┐
  └─ DELETE 旧缓存                │
                                  ▼
                      ┌─────────────────────┐
                      │    RabbitMQ Broker   │
                      │  DirectExchange      │
                      │  Queue (durable)     │
                      └──────────┬──────────┘
                                 │
                                 ▼
RecommendCacheConsumer.refreshCache(userId)    ← @RabbitListener
  │
  ▼
AIRecommendServiceImpl.refreshCandidateCache(userId)
  ├─ DB: findMatchedUserIds(userId, 50)
  ├─ DB: 批量查 users + tags
  └─ Redis: HSET pool + cursor（Lua 原子化，TTL 3h）
```

## 配置类决定的 Broker 侧结构

```
RabbitMQConfig.java
  ├─ Queue("recommend.cache.queue", durable=true)     → Broker 队列定义
  ├─ DirectExchange("recommend.cache.exchange")        → Broker 交换机定义
  ├─ Binding(queue ← routingKey="recommend.cache.refresh" ← exchange)
  └─ Jackson2JsonMessageConverter                      → 消息 JSON 序列化
```

## 包职责

| 包 | 文件 | 角色 |
|----|------|------|
| `mq/` | RabbitMQConfig | 拓扑声明（启动时同步到 Broker） |
| `mq/` | RecommendCacheConsumer | 纯消费者，只做委托，无业务逻辑 |
| `event/` | TagChangedEvent | Spring 事件载体，不依赖 MQ API |
| `listener/` | TagChangedListener | 生产端守门人：去重 + 发 MQ + 删缓存 |

## 核心设计决策

- **DirectExchange** — 只有 1 个队列 1 个路由键，不需要 Topic 的通配符
- **消息体只传 userId** — 不包装 DTO，消费者接到后自己查库（避免数据不一致）
- **无 DLQ/无重试** — 缓存是衍生数据，MQ 失败由 `/ai/recommend/{id}` 的缓存 miss 路径兜底
- **AUTO ACK + no-requeue** — 消费失败直接丢弃，不无限重试
- **Jackson2JsonMessageConverter** — 替换默认 Java 序列化，消息在 Management UI 可读
