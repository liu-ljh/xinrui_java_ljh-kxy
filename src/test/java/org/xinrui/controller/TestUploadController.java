package org.xinrui.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinrui.schedule.PacsInfectionSchedule;

import javax.annotation.Resource;

/**
 * 测试用的接口，用于手动触发上传任务（避免依赖定时器）
 */
@RestController
@RequestMapping("/test")
public class TestUploadController {

	@Resource
	private PacsInfectionSchedule pacsInfectionSchedule;

	@PostMapping("/upload")
	public String triggerUpload() {
		pacsInfectionSchedule.uploadEmrExClinicalData();
		return "Upload task triggered successfully";
	}
}
