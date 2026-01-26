package org.xinrui.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.dto.ResultDTO;
import org.xinrui.dto.EmrExClinicalItemDto;

@Slf4j
@RestController
@RequestMapping("/hclient/emr/receive")
public class TestReceiveController {

	// 接收POST请求的端点
	@PostMapping("/emrExClinical")
	public ResponseEntity<ResultDTO> receiveData(@RequestBody EmrExClinicalDto clinical) {
		try {
			log.info("接收到临床项目数据: {}", clinical);
			System.out.println("接收到临床项目数据: "+ clinical);

			// 这里可以添加你的业务逻辑处理
			// 示例：返回成功响应
			ResultDTO response = new ResultDTO(
				true,
				"数据接收成功",
				clinical.getId()
			);
			System.out.println("返回请求响应 "+ response);
			log.info("返回请求响应 {}", response);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("处理请求时发生错误", e);
			ResultDTO errorResponse = new ResultDTO(
				false,
				"PROCESS_ERROR",
				"数据处理异常",
				"服务器内部错误",
				clinical.getId()
			);
			log.info("返回请求响应 {}", errorResponse);
			System.out.println("返回请求响应 "+ errorResponse);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
}
