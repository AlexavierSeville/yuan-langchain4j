package com.yuan.yuanlangchain4j.controller;

import com.yuan.yuanlangchain4j.assistant.YuanMedicalAgent;
import com.yuan.yuanlangchain4j.bean.MedicalForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * YuanMedicalController - com.yuan.yuanlangchain4j.controller
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 10:36
 */

@Tag(name = "中医元仔")
@RestController
@RequestMapping("/yuan")
public class YuanMedicalController {

    @Resource
    private YuanMedicalAgent yuanMedicalAgent;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody MedicalForm medicalForm){
        return yuanMedicalAgent.chat(medicalForm.getMemoryId(), medicalForm.getMessage());
        //您好！😊 我是工作于元仔中医医院的智能顾问与伴诊助手“中医元仔”🌿，很高兴为您服务！
        // 我擅长用简单易懂的方式解答中医药相关问题，比如体质调理、药材功效、中医术语等。也可以协助就医流程（如分诊建议、挂号查询等）。
        // **温馨提醒**：
        // 1️⃣ 我的回答仅供参考，不能替代专业医生的诊断治疗
        // 2️⃣ 如有严重不适，请及时线下就医🏥
        // 请问有什么可以帮您的呢？✨（比如：想了解某个中医知识？或需要就医建议？）

    }
}
