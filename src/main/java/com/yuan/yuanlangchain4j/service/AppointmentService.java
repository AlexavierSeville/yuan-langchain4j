package com.yuan.yuanlangchain4j.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuan.yuanlangchain4j.entity.Appointment;

/**
 * AppointmentService - com.yuan.yuanlangchain4j.service
 * <p>
 * 描述：此类由 Alexavier·元仔 创建
 *
 * @author Alexavier·元仔
 * @see <a href="https://github.com/AlexavierSeville">GitHub: AlexavierSeville</a>
 * @since 2025/6/4 11:42
 */

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
