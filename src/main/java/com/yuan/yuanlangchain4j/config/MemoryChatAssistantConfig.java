package com.yuan.yuanlangchain4j.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
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
public class MemoryChatAssistantConfig{
    // 这个bean对象用于注入到MemoryChatAssistant中的
    // @AiService的chatMemory属性中
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
