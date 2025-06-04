package com.yuan.yuanlangchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * YuanMedicalAgent - com.yuan.yuanlangchain4j.assistant
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 09:58
 */
@AiService(
        wiringMode = EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",  // 更换为阿里通义千问（流式）
        chatMemoryProvider = "yuanMedicalMemoryProvider",
        tools = "appointmentTools",
        contentRetriever = "yuanMedicalContentRetrieverPinecone"  // 这样我的ai就有从外部知识库检索数据的能力了
)
public interface YuanMedicalAgent {
    @SystemMessage(fromResource = "medical-agent-prompt.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);
}
