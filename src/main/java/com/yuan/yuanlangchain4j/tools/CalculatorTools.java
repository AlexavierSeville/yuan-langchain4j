package com.yuan.yuanlangchain4j.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.stereotype.Component;

/**
 * CalculatorTools - com.yuan.yuanlangchain4j.tools
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 10:55
 */
@Component
public class CalculatorTools {
    // 名称定义一定要足够清晰便于大模型识别，业务流程太多的时候就需要加上name，注意：name在2025.6的langchain不能使用中文
    // value是补充说明，但是容易导致大模型幻觉，谨慎使用
    // @P是参数的注解，required是是否必填，默认为true
    // @ToolMemoryId 用于标记“工具调用参数中的 memoryId”，以便工具调用（Tool Calling）中与 ChatMemory 关联，实现带记忆的工具交互。
    @Tool(name = "sum", value = "将两个参数a和b相加并返回运算结果")
    double sum(
            @ToolMemoryId int memoryId,
            @P(value = "加数1", required = true) double a,
            @P(value = "加数2") double b) {
        System.out.println("调用加法运算 MemoryId:" + memoryId);
        return a + b;
    }
    @Tool(name = "square")
    double squareRoot(@ToolMemoryId int memoryId, double x) {
        System.out.println("调用平方根运算 MemoryId:" + memoryId);
        return Math.sqrt(x);
    }
}
