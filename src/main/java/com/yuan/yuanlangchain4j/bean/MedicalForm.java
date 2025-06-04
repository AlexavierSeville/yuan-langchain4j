package com.yuan.yuanlangchain4j.bean;

import lombok.Data;

/**
 * MedicalForm - com.yuan.yuanlangchain4j.bean
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 10:35
 */
@Data
public class MedicalForm {
    private Long memoryId;  // 会话id
    private String message;  // 用户问题
}
