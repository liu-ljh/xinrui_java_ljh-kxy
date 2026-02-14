package org.xinrui.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.xinrui.core.tool.utils.Func;
import org.xinrui.dto.ResultDTO;


/**
 * @author Administrator
 */
public class CommonUtil {

	/**
	 * 发送POST请求
	 * @param apiPath
	 * @param dto
	 * @return
	 */
	public static String sendPostRequest(String apiPath, Object dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestBody = Func.toJson(dto);

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.postForObject(apiPath, new HttpEntity<>(requestBody, headers), String.class);
	}


	/**
	 * 将json字符串转换成ResultDTO对象
	 * @param result
	 * @return
	 */
	public static ResultDTO convertToResultDTO(String result) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(result);
			ResultDTO resultDTO = new ResultDTO();

			resultDTO.setResult(jsonObject.getBoolean("result"));
			resultDTO.setDesc(jsonObject.getString("desc"));
			resultDTO.setId(jsonObject.getString("id"));

			// 提取错误相关字段
			if (!resultDTO.getResult()) {
				resultDTO.setErrorCode(jsonObject.getString("errorCode"));
				resultDTO.setErrorName(jsonObject.getString("errorName"));
			}
			return resultDTO;
		} catch (Exception e) {
			return new ResultDTO(false,"", "invalid_response_format", "响应格式错误", "");
		}

	}
}
