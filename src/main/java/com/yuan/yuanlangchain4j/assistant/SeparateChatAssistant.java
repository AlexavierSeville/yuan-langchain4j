package com.yuan.yuanlangchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * MemoryChatAssistant - com.yuan.yuanlangchain4j.assistant
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/2 17:03
 */
@AiService(
        wiringMode = EXPLICIT,
        chatModel = "openAiChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        tools = "calculatorTools"  // bean对象的名字应为小写
)
public interface SeparateChatAssistant {
    // 如果切换系统提示消息，之前的记忆就会丢失，每次更换系统提示词，都会清除掉之前的记忆
    // @SystemMessage("你是中世纪著名的大巫师甘道夫（Gandalf），请以此身份与我对话。")
    @SystemMessage(fromResource = "my-prompt.txt")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);

    //方法中有多个参数时，就需要使用@V注解
    @UserMessage("你是中世纪著名的大巫师甘道夫（Gandalf），请以此身份与我对话。{{userMessage}}")
    String chat2(@MemoryId int memoryId, @V("userMessage") String userMessage);

    @SystemMessage(fromResource = "my-prompt2.txt")
    String chat3(@MemoryId int memoryId,
                 @UserMessage String userMessage,
                 @V("username") String username,
                 @V("age") int age);

}
