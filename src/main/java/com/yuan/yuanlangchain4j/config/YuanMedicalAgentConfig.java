package com.yuan.yuanlangchain4j.config;

import com.yuan.yuanlangchain4j.store.MongoChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * YuanMedicalAgentConfig - com.yuan.yuanlangchain4j.config
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 10:26
 */
@Configuration
public class YuanMedicalAgentConfig {

    @Resource
    private MongoChatMemoryStore mongoChatMemoryStore;

    // 注意ChatMemoryProvider的名字要和@AiService中的chatMemoryProvider名字一样
    @Bean
    public ChatMemoryProvider yuanMedicalMemoryProvider() {
        /*
         等价写法:
         return new ChatMemoryProvider() {
             @Override
             public ChatMemory get(Object memoryId) {
                 return MessageWindowChatMemory.builder()
                         .id(memoryId)
                         .maxMessages(25)
                         .chatMemoryStore(mongoChatMemoryStore)
                         .build();
             }
         };
        */

        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(25)
                .chatMemoryStore(mongoChatMemoryStore)
                .build();
    }

    // @Bean
    // ContentRetriever yuanMedicalContentRetriever() {
    //     //使用FileSystemDocumentLoader读取指定目录下的知识库文档
    //     //并使用默认的文档解析器对文档进行解析
    //     Document document1 = FileSystemDocumentLoader.loadDocument("knowledge/yuan_hospital.md");
    //     Document document2 = FileSystemDocumentLoader.loadDocument("knowledge/yuan_hospital_departments.md");
    //     Document document3 = FileSystemDocumentLoader.loadDocument("knowledge/yuan_Chinese_Internal_Medicine_Detail.md");
    //     List<Document> documents = Arrays.asList(document1, document2, document3);
    //     //使用内存向量存储
    //     InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
    //     //使用默认的文档分割器
    //     //按段落分割文档：每个片段包含不超过 300个token，并且有 30个token的重叠部分保证连贯性
    //     //注意：当段落长度总和小于设定的最大长度时，就不会有重叠的必要。
    //     EmbeddingStoreIngestor.ingest(documents, embeddingStore);
    //     //从嵌入存储（EmbeddingStore）里检索和查询内容相关的信息
    //     return EmbeddingStoreContentRetriever.from(embeddingStore);
    // }

    @Resource
    private EmbeddingStore embeddingStore;

    @Resource
    private EmbeddingModel embeddingModel;

    @Bean
    ContentRetriever yuanMedicalContentRetrieverPinecone(){
        // 创建一个 EmbeddingStoreContentRetriever 对象，用于从嵌入存储中检索内容
        return EmbeddingStoreContentRetriever
                .builder()
                // 设置用于生成嵌入向量的嵌入模型
                .embeddingModel(embeddingModel)
                // 指定要使用的嵌入存储
                .embeddingStore(embeddingStore)
                // 设置最大检索结果数量，这里表示最多返回 1 条匹配结果
                .maxResults(1)
                // 设置最小得分阈值，只有得分大于等于 0.77 的结果才会被返回
                .minScore(0.77)
                // 构建最终的 EmbeddingStoreContentRetriever 实例
                .build();
    }



}
