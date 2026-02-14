package org.xinrui.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xinrui.service.IPacsIdissService;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/pacs-idiss")
@RequiredArgsConstructor
@Api(tags = "PACS传染病上报测试接口")
public class PacsIdissTestController {

	private final IPacsIdissService pacsIdissService;

	/**
	 * 测试单个报告数据上传
	 */
	@PostMapping("/uploadEmrExClinicalData")
	public void uploadEmrExClinicalData() {
		pacsIdissService.uploadEmrExClinicalData();
	}

	/**
	 * 测试单个报告数据上传
	 */
	@PostMapping("/uploadEmrExClinicalItemData")
	public void uploadEmrExClinicalItemData() {
		pacsIdissService.uploadEmrExClinicalItemData();
	}


}
