 package com.yuan.yuanlangchain4j.config;

import com.yuan.yuanlangchain4j.store.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MemoryChatAssistantConfig - com.yuan.yuanlangchain4j.config
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/2 17:06
 */

@Configuration
public class SeparateChatAssistantConfig {

    @Resource
    private MongoChatMemoryStore mongoChatMemoryStore;

    // 这个bean对象用于注入到MemoryChatAssistant中的
    // @AiService的chatMemory属性中
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(10)
                // .chatMemoryStore(new InMemoryChatMemoryStore())  // 这样配置使聊天记忆以键值对形式存在内存
                .chatMemoryStore(mongoChatMemoryStore)  // 这样配置使聊天记忆以键值对形式存在mongodb
                .build();
    }
}
