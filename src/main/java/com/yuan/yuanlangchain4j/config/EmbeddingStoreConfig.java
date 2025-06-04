package com.yuan.yuanlangchain4j.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EmbeddingStoreConfig - com.yuan.yuanlangchain4j.config
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 21:07
 */
//向量云存储
@Configuration
public class EmbeddingStoreConfig {

    @Resource
    private EmbeddingModel embeddingModel;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(){
        //创建向量存储
        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey(System.getenv("PINECONE"))
                .index("yuan-medical")  //如果指定的索引不存在，将创建一个新的索引
                .nameSpace("yuan-medical-namespace")  //如果指定的名称空间不存在，将创建一个新的名称空间
                .createIndex(PineconeServerlessIndexConfig.builder()
                        .cloud("AWS")
                        .region("us-east-1")
                        .dimension(embeddingModel.dimension())  //指定索引的向量维度，该维度与 embeddedModel 生成的向量维度相同。
                        .build())
                .build();

        return embeddingStore;
    }

}
