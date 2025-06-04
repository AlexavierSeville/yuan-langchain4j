# **项目概述**
- “中医元仔”智能医疗机器人，面向中医诊疗场景，集成对话问诊、知识检索、预约挂号与取消等功能。
- 基于 LangChain4j 与阿里通义千问（DashScope 平台）构建 RAG（检索增强生成）流程，实现专业中医知识支持及工具调用。
- 具体教程可见个人博客：https://alexavieryuan.us.kg/archives/langchain4j-yi-liao-dui-hua-ai
---

## 主要功能
1. **多轮对话与会话记忆**
    - 使用 Java 17 + Spring Boot + WebFlux 构建 Reactive API，暴露 `/yuan/chat` 接口，支持 Flux<String> 流式推送答案。
    - ChatMemoryProvider（MongoChatMemoryStore + MessageWindowChatMemory）将用户多轮对话历史持久化到 MongoDB，保留最近 25 条消息。
    - 用户可在同一会话中持续提问，中医元仔能够结合上下文回答。

2. **知识检索（RAG）**
    - 使用 FileSystemDocumentLoader 从 `knowledge/` 目录批量加载 Markdown 文档（如中医典籍、科室介绍、病症详解等）。
    - DocumentByParagraphSplitter + HuggingFaceTokenizer 将每个文档按段落拆分，最大 300 token，重叠 30 token，再生成 TextSegment。
    - EmbeddingStoreIngestor 将 TextSegment 送入 DashScope text-embedding-v3 模型生成向量，存入 Pinecone EmbeddingStore（或 InMemoryEmbeddingStore）。
    - ContentRetriever（EmbeddingStoreContentRetriever）在对话过程中把用户问题嵌入后检索最相似的中医知识片段，并将片段注入 prompt，让 LLM 生成更专业的回答。

3. **预约工具集成**
    - 自定义 `AppointmentTools` 类，包含 `@Tool` 注解的三个方法：
        - `QueryAvailability(String 科室名称, String 日期, String 时间, String 医生名称)`：检查号源是否可预约；
        - `BookAppointment(Appointment)`：确认号源后执行预约操作，将记录写入 MySQL 数据库；
        - `CancelAppointment(Appointment)`：根据条件删除预约记录。
    - LLM 在生成回答时，当需要挂号或取消挂号时，会输出工具调用标记，框架拦截后反射执行对应方法，再将结果反馈给 LLM，完成“交互式”业务流程。

4. **流式输出**
    - 集成阿里通义千问（Qwen）流式聊天模型（qwen-plus），作为 `StreamingChatModel`，实现边生成边推送。
    - Controller 返回 `Flux<String>`，前端可通过 SSE 或 Chunked 实时呈现生成内容，提升用户体验。
    - 若不需要流式，也可切换为非流式模型（qwen-max），方法签名从 Flux 改为 Mono 或 String。

5. **API 文档与测试**
    - 使用 Springdoc OpenAPI（Swagger 注解）自动生成在线 API 文档，标注 `@Tag`、`@Operation`，方便前后端联调。
    - JUnit 测试涵盖：
        - Token 统计示例（HuggingFaceTokenizer 计算 token 数），
        - 文档分割与嵌入示例，
        - 工具方法单元测试，确保数据库读写与并发安全。

---

## 技术栈
- **语言与框架**
    - Java 17、Spring Boot 3.x、Spring WebFlux（Reactor）、Spring Data MongoDB
    - Spring Data JPA / MyBatis-Plus 负责 MySQL 业务表（预约记录）增删改查
    - Lombok 简化实体类与 DTO 编写

- **LLM 与 RAG**
    - LangChain4j（核心依赖：`langchain4j-core`、`langchain4j-community-dashscope`）
    - HuggingFaceTokenizer（基于 BPE 分词）
    - DashScope 平台：
        - qwen-max（非流式聊天模型）
        - qwen-plus（流式聊天模型）
        - text-embedding-v3（文本向量化模型）

- **向量存储**
    - Pinecone EmbeddingStore（生产环境）
    - InMemoryEmbeddingStore（测试/演示环境）

- **数据库**
    - MongoDB：存储多轮对话记忆，使用 Spring Data MongoDB 驱动；
    - MySQL：存储预约、用户、医生排班等业务数据，使用 MyBatis-Plus 或 Spring Data JPA。

- **构建与文档**
    - Maven 多模块管理，模块包括：`api`、`service`、`dao`、`web`
    - Springdoc OpenAPI（自动生成 Swagger UI）

- **其他**
    - 配置管理：Spring Boot 配置文件（application.properties / application.yml）
    - 日志：Logback，日志级别在配置中可调整为 DEBUG/INFO
    - 容器化部署：Docker + Docker Compose，包含后端服务、MongoDB、MySQL、Pinecone（或模拟服务）
    - 版本控制：Git（GitHub 仓库），代码审查与 CI/CD（GitHub Actions）

