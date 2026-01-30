package org.xinrui.service.impl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.xinrui.config.ServerEndpointConfig;
import org.xinrui.core.tool.utils.DateUtil;
import org.xinrui.dto.EmrExClinicalItemDto;
import org.xinrui.util.CommonUtil;
import org.xinrui.core.tool.utils.Func;
import org.xinrui.dto.EmrExClinicalDto;

import org.xinrui.dto.ResultDTO;
import org.xinrui.service.IPacsInfectionService;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.xinrui.schedule.PacsInfectionSchedule.REDIS_INFECTION_ITEM_DATA;
import static org.xinrui.schedule.PacsInfectionSchedule.REDIS_INFECTION_REPORT_DATA;


@Slf4j
@AllArgsConstructor
@Service
public class PacsInfectionServiceImpl implements IPacsInfectionService {

	private ServerEndpointConfig serverEndpointConfig;
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean postEmrExClinical(EmrExClinicalDto emrExClinicalDatum) {
		String json = Func.toJson(emrExClinicalDatum);
		if(json==null){
			log.error("数据为null，Json序列化失败{}",emrExClinicalDatum);
			return false;
		}
		JSONObject requestBody = JSON.parseObject(json);

		try {
			//todo
			String apiUrl = serverEndpointConfig.getUrl() + "/hclient/emr/receive/emrExClinical";
			//测试使用testUrl
//			String apiUrl = "http://localhost:8081/hclient/emr/receive/emrExClinical";
			String result = CommonUtil.sendPostRequest(apiUrl, requestBody);
			ResultDTO resultDTO = CommonUtil.convertToResultDTO(result);

			// 处理响应
			if (resultDTO != null && resultDTO.getResult()){
				log.info("【uploadEmrExClinicalApi】上传成功：param:{}, result:{}",
					Func.toJson(emrExClinicalDatum),
					Func.toJson(resultDTO));
				return true;
			} else {
				log.error("【uploadEmrExClinicalItemApi】上传失败：param:{}, result:{}",
					Func.toJson(emrExClinicalDatum),
					Func.toJson(resultDTO));
				return false;
			}
		} catch (Exception e){
			log.error("【postEmrExClinicalItem】请求异常：param:{}, errMsg:{}",
					Func.toJson(emrExClinicalDatum), e);
			return false;
		}
	}

	@Override
	public boolean postEmrExClinicalItem(EmrExClinicalItemDto emrExClinicalItemDatum) {
		String json = Func.toJson(emrExClinicalItemDatum);
		if(json==null){
			log.error("数据为null，Json序列化失败{}",emrExClinicalItemDatum);
			return false;
		}
		JSONObject requestBody = JSON.parseObject(json);
		try {
			String apiUrl = serverEndpointConfig.getUrl()+"/hclient/emr/receive/emrExClinicalItem";
			String result = CommonUtil.sendPostRequest(apiUrl, requestBody);
			ResultDTO resultDTO = CommonUtil.convertToResultDTO(result);

			// 处理响应
			if (resultDTO != null && resultDTO.getResult()){
				log.info("【uploadEmrExClinicalItemApi】上传成功：param:{}, result:{}",
					Func.toJson(emrExClinicalItemDatum),
					Func.toJson(resultDTO));
				return true;
			} else {
				log.error("【uploadEmrExClinicalItemApi】上传失败：param:{}, result:{}",
					Func.toJson(emrExClinicalItemDatum),
					Func.toJson(resultDTO));
				return false;
			}
		} catch (Exception e){
			log.error("【postEmrExClinicalItem】请求异常：param:{}, errMsg:{}",
					Func.toJson(emrExClinicalItemDatum), e);
			return false;
		}
	}

	@Override
	public void clearRedis() {
		Date now = new Date();
		String uploadExClinicalItemRedisKey = REDIS_INFECTION_ITEM_DATA + ":" + DateUtil.format(DateUtil.minusDays(now,16), "yyyyMMdd");
		String uploadExClinicalRedisKey = REDIS_INFECTION_REPORT_DATA + ":" + DateUtil.format(DateUtil.minusDays(now,16), "yyyyMMdd");
		stringRedisTemplate.delete(uploadExClinicalRedisKey);
		stringRedisTemplate.delete(uploadExClinicalItemRedisKey);
	}

}
