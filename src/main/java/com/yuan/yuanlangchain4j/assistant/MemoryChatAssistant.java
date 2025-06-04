package com.yuan.yuanlangchain4j.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

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
        chatMemory = "chatMemory"
)
public interface MemoryChatAssistant {
    // 使用V注解，将userMessage的值传递给m变量（在有多个参数时必须使用）
    @UserMessage("我是你的好朋友，请用粤语和我说话，并使用丰富的表情符号。 {{userMessage}}")
    String chat(@V("userMessage") String userMessage);
}
