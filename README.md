
## 基于分布式架构的T+0量化交易平台

#### 采用技术

SpringCloudAlibaba、SpringBoot、Mybatis、MySQL、Redis、RabbitMQ、SpringSecurity、TypeScript、Git、Vue

#### 项目概述

该项目是一款 T+0 交易委托、交易执行中台、行情、量化交易、交易数据中台模块于一体的安全量化交易平台，用户可以进行模拟交易委托，并可自定化简单的量化交易策略，系统自动执行交易任务

#### 项目架构

- nacos-config       Nacos配置

- trade-auth        授权认证中心

- trade-base        项目工具类

- trade-checkcode   验证中心

- trade-gateway     网关

- trade-message-sdk 消息中间件

- trade-parent      项目父工程

- trade-sql         数据库

- trade-transaction 交易中心
	- trade-transaction-api      交易对外API
	- trade-transaction-data     交易数据中心
	- trade-transaction-exec     交易执行中心
	- trade-transaction-model    交易数据模型（DTO + PO + VO）
	- trade-transaction-service  交易服务（service + mapper + client）

- trade-web         交易前端项目
	- api                   接口封装
	- assets                静态资源
	- components            通用组件
	- router                路由
	- state                 状态管理
	- views                 页面

#### 项目介绍

- 技术组负责人，负责数据库设计，完全自主进行交易委托、交易执行、行情持久化、账户充值的业务开发
- 采用 SpringSecurity + OAuth2 进行账户鉴权，Nacos 作为注册配置中心，Gateway 统一 API 调用并实现 CORS
- 使用 Redis 建立多级缓存，防止缓存雪崩，并采用 RabbitMQ 设置定时交易任务，执行中台进行委托交易
