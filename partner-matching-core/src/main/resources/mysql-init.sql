CREATE DATABASE IF NOT EXISTS parter_matcher;
USE partner_matcher;

-- 用户表
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY, -- 用户ID
    qq_email VARCHAR(32) NOT NULL UNIQUE, -- QQ邮箱
    username VARCHAR(32) NOT NULL UNIQUE, -- 用户名
    password BINARY(32) NOT NULL, -- SHA256混淆密码
    gender TINYINT, -- 性别
    avatar_uri TEXT, -- 头像URI
    profile TEXT, -- 个人简介
    collect_number INT NOT NULL DEFAULT 0, -- 收藏数（需要联合user_collections）
    tags VARCHAR(47) TEXT NOT NULL DEFAULT '[]', -- 标签（json array）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 注册时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);

-- 用户收藏表
CREATE TABLE user_collections (
    id INT AUTO_INCREMENT PRIMARY KEY, -- 收藏ID
    user_id INT NOT NULL, -- 用户ID
    collect_user_id INT NOT NULL, -- 被收藏的用户ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 收藏时间
    INDEX user_id_index (user_id)
);

-- 用户标签表
CREATE TABLE user_tag(
    user_id BIGINT NOT NULL COMMENT '用户ID，关联user表的id',
    tag_id  INT NOT NULL COMMENT '标签ID，关联tags表的id',
    PRIMARY KEY (user_id, tag_id),
    constraint user_tag_ibfk_1
        foreign key (user_id) references user (id)
            on delete cascade,
    constraint user_tag_ibfk_2
        foreign key (tag_id) references tags (id)
            on delete cascade
)comment '用户-标签关联表';

create index tag_id
    on user_tag (tag_id);

-- 标签表
CREATE TABLE tags(
    id INT AUTO_INCREMENT PRIMARY KEY, -- 标签ID
    tag_name VARCHAR(8) NOT NULL UNIQUE, -- 标签名称
    tag_type VARCHAR(8) NOT NULL, -- 标签类型
    INDEX tag_type_index (tag_type)
);

CREATE TABLE `api_log` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint DEFAULT NULL COMMENT '请求用户ID',
    `api_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
    `request_params` text COMMENT '请求参数',
    `response_result` text COMMENT '响应结果',
    `cost_time` bigint DEFAULT NULL COMMENT '耗时(ms)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
    PRIMARY KEY (`id`)
);

-- 队伍表
CREATE TABLE teams (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- 队伍ID
    creator_user_id INT NOT NULL, -- 创建者用户ID
    team_name VARCHAR(32) NOT NULL, -- 队伍名称
    password VARCHAR(8), -- 密码
    description TEXT, -- 描述
    max_num INT NOT NULL DEFAULT 10, -- 最大人数
    expire_time TIMESTAMP, -- 过期时间
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
    is_deleted TINYINT NOT NULL DEFAULT 0, -- 是否删除
    INDEX creator_user_id_index (creator_user_id),
    INDEX team_name_index (team_name)
);
