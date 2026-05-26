# 伙伴匹配系统

基于标签匹配 + 向量搜索 + AI 推荐的伙伴匹配系统。

## 项目结构

```
partner-matching/                    ← 父 POM，仅做版本管理
├── pom.xml
├── .mvn/ mvnw mvnw.cmd             ← Maven Wrapper，无需安装 Maven
│
├── partner-matching-rag/            ← RAG 引擎库（不可独立运行）
│   └── src/main/java/.../rag/
│       ├── RAGTransferService.java       ← ONNX 文本 → float[] 向量
│       ├── LuceneStorageService.java     ← Lucene 索引读写 + KNN 向量搜索
│       └── pojo/LuceneSearchResult.java
│
├── partner-matching-core/           ← Spring Boot 应用（依赖 rag 模块）
│   ├── pom.xml
│   ├── model.onnx / model.onnx_data / tokenizer.json  ← 模型文件（需自行获取）
│   └── src/main/java/.../partnermatch/
│       ├── PartnerMatchApplication.java  ← 启动入口
│       ├── common/Result.java            ← 统一响应 {code, message, data}
│       ├── config/AiTimeoutConfig.java   ← AI 超时配置
│       ├── controller/
│       │   ├── AIRecommendController.java ← GET /ai/recommend/{id}
│       │   └── TagController.java         ← PUT /api/tag/{userId}
│       ├── dto/                          ← 前端传输对象
│       ├── entity/                       ← 数据库实体（User, Tag, UserTag）
│       ├── event/TagChangedEvent.java    ← 标签变更事件
│       ├── listener/TagChangedListener.java ← 事件处理：删缓存 + 发 MQ
│       ├── mapper/                       ← MyBatis Plus 数据访问层
│       ├── mq/
│       │   ├── RabbitMQConfig.java       ← 队列/交换机/绑定声明
│       │   └── RecommendCacheConsumer.java ← MQ 消费者：异步重建缓存
│       └── service/
│           ├── AIRecommendService.java   ← 推荐接口
│           ├── AIChatService.java        ← AI 对话接口
│           ├── TagService.java           ← 标签接口
│           └── impl/                     ← 实现类
│
└── rag_index/                                   ← Lucene 索引（运行时自动生成）
```

## 各模块职责

### partner-matching-rag（RAG 模块）

| 文件 | 职责 |
|------|------|
| `RAGTransferService.java` | 加载 ONNX 模型，将标签文本编码为 float 向量 |
| `LuceneStorageService.java` | 管理本地 Lucene 索引，支持标签搜索和 KNN 向量搜索 |
| `LuceneSearchResult.java` | 搜索结果 POJO（userIds + 游标） |

### partner-matching-core（业务模块）

| 文件 | 职责 |
|------|------|
| `AIRecommendServiceImpl.java` | 核心推荐逻辑：缓存读取 → 向量搜索 → SQL 降级 → AI 推荐 |
| `TagServiceImpl.java` | 标签增删改，事务操作后发布 TagChangedEvent |
| `TagChangedListener.java` | 接收事件，去重 → 删缓存 → 发 MQ 预热 |
| `RecommendCacheConsumer.java` | MQ 异步消费者，调用 refreshCandidateCache 重建缓存池 |
| `RabbitMQConfig.java` | 声明 recommend.cache.queue/exchange/binding |

## 

### 推荐 + 标签开发
- `AIRecommendServiceImpl` — 推荐主流程（缓存、向量搜索、AI 调用）
- `TagServiceImpl` + `TagChangedListener` — 标签 CRUD 和缓存刷新
- 碰到 ONNX/Lucene 报错时联系 RAG 模块维护者

### RAG 模块维护者（模型 + 索引）
- `RAGTransferService` — ONNX 模型加载和编码
- `LuceneStorageService` — Lucene 索引读写、KNN 搜索
- 模型文件（model.onnx / model.onnx_data / tokenizer.json）需要你提供

## 怎么启动

### 前置条件

1. **JDK 17+**，`java -version` 确认
2. **MySQL** 本地运行在 `localhost:3306`，已建库 `friends`（表已就绪）
3. **Redis** 已启动，地址在 `application.yml` 中配置
4. **RabbitMQ**（可选）— 不需要可设 `rabbitmq.enabled: false`
5. **AI 推荐**（可选）— 不需要可在 config.yml 跳过 openai 配置
6. **config.yml** — 自行在 `partner-matching-core/src/main/resources/` 下创建：

**最小配置（不需要 MQ 和 AI）：**
```yaml
config:
  mysql:
    username: root
    password: 你的密码
  redis:
    password: 你的密码
```

**完整配置：**
```yaml
config:
  mysql:
    username: root
    password: 你的密码
  redis:
    password: 你的密码
  rabbitmq:        # 可选
    username: user
    password: 你的密码
  openai:           # 可选
    api-key: 你的APIKey
```

### 启动步骤

```bash
# 1. 进入项目目录
cd partner-matching

# 2. 编译（首次运行）
./mvnw clean install -DskipTests

# 3. 启动
./mvnw spring-boot:run -pl partner-matching-core
```

启动后访问：`http://localhost:8080/ai/recommend/{userId}`

## 配置文件说明

| 文件 | 用途 | 是否提交 |
|------|------|---------|
| `application.yml` | 非敏感配置：数据库地址、Redis 地址、RabbitMQ 地址、超时 | **是** |
| `config.yml` | 敏感配置：所有密码和 API Key | **否**（gitignored） |

`application.yml` 通过 `spring.config.import: classpath:config.yml` 导入 config.yml 中的值，占位符格式为 `${config.xxx}`。这样每个人的密码不会提交到仓库。

## 索引文件

`rag_index/` 是 Lucene 运行时自动生成的本地索引，**不纳入 Git**。首次启动后如果索引为空，推荐接口会自动降级到 SQL 查询。需要回填索引时联系 RAG 模块维护者。  

**！！！**

**经本人亲身实践**: 三个对模型的配置 需要在根目录 partner-matching/ 和子模块partner-matching-rag/ 各放一份才能顺利启动 并且rag_index需要放在根目录下（rag_index没有时启动后会自动添加）
