package org.xinrui.schedule;

import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.guangzhou.link.service.PacsGuangZhouLinkServiceImpl;
import org.guangzhou.link.vo.PacsPatientExtVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xinrui.core.tool.utils.DateUtil;
import org.xinrui.core.tool.utils.Func;
import org.xinrui.pacs.entity.PacsExamReport;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * pacs 向 his 回写报告
 * @author jxl
 * */
@Slf4j
@Component
public class PacsToHisSchedule {


	@Autowired
	private PacsGuangZhouLinkServiceImpl pacsGuangZhouLinkService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Value("${hisReport.webserviceUrl}")
	private String hisReportWebserviceUrl;

	@Value("${hisReport.methodName}")
	private String METHOD_NAME;

	@Value("${hisReport.nameSpaceUrl}")
	private String NAME_SPACE_URL;

	private static final String REDIS_HIS_TO_REPORT = "pacs3rd:his2report:";

	private SoapClient soapClient;

	/** SOAP响应数据解析 常量定义 */
	private static final Pattern FAULT_CODE_PATTERN = Pattern.compile("<faultcode[^>]*>([^<]+)</faultcode>");
	private static final Pattern FAULT_STRING_PATTERN = Pattern.compile("<faultstring[^>]*>([^<]+)</faultstring>");
	private static final Pattern RESULT_PATTERN = Pattern.compile(
		"<getpacsresultResult[^>]*>\\s*(\\[\\{.*?\\}\\])\\s*</getpacsresultResult>",
		Pattern.DOTALL
	);


	/**
	 * 对于近两天的数据
	 * 每2分钟执行一次
	 */
	@Scheduled(cron="0 0/2 * * * ?")
	public void current(){
		Date now = new Date();
		int pageNum = 0;
		int size = 100;
		while(true) {
			pageNum ++;
			Page<PacsExamReport> pacsExamReportPage = pacsGuangZhouLinkService.pageReport(pageNum, size, DateUtil.minusDays(now, 2), now);
			if(pageNum > pacsExamReportPage.getTotal() || Func.isEmpty(pacsExamReportPage.getRecords())){
				return;
			}
			for (PacsExamReport report : pacsExamReportPage.getRecords()) {
				try{
					String redisKey = REDIS_HIS_TO_REPORT + DateUtil.formatDate(report.getReportDate());
					if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(redisKey, report.getRegistrationOid().toString()))) {
						continue;
					}
					sendReeportToHis(report.getRegistrationOid());
					stringRedisTemplate.opsForSet().add(redisKey, report.getRegistrationOid().toString());
				}catch (Exception e){
					log.error("上传报告数据异常, registrationOid:{}",report.getRegistrationOid(), e);
				}
			}
		}
	}

	/**
	 * 对于历史同步的数据
	 * 每小时执行一次
	 */
	@Scheduled(cron="0 0 0/1 * * ?")
	public void execute(){
		Date now = new Date();
		int pageNum = 0;
		int size = 100;
		while(true) {
			pageNum ++;
			Page<PacsExamReport> pacsExamReportPage = pacsGuangZhouLinkService.pageReport(pageNum, size, DateUtil.minusDays(now, 15), DateUtil.minusDays(now, 2));
			if(pageNum > pacsExamReportPage.getTotal() || Func.isEmpty(pacsExamReportPage.getRecords())){
				return;
			}
			for (PacsExamReport report : pacsExamReportPage.getRecords()) {
				try{
					String redisKey = REDIS_HIS_TO_REPORT + DateUtil.formatDate(report.getReportDate());
					if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(redisKey, report.getRegistrationOid().toString()))) {
						continue;
					}
					sendReeportToHis(report.getRegistrationOid());
					stringRedisTemplate.opsForSet().add(redisKey, report.getRegistrationOid().toString());
				}catch (Exception e){
					log.error("上传报告数据异常, registrationOid:{}",report.getRegistrationOid(), e);
				}
			}
		}
	}

	@Scheduled(cron="0 0 1 * * ?")
	public void clearCache(){
		List<String> keys = new ArrayList<>();
		for (int i = 16; i < 30; i++) {
			Date date = DateUtil.minusDays(new Date(),i );
			keys.add(REDIS_HIS_TO_REPORT + DateUtil.formatDate(date));
		}
		stringRedisTemplate.delete(keys);
	}

	public void sendReeportToHis(Long registrationOid){
		// 1. 获取报告数据
		PacsPatientExtVO pacsData = pacsGuangZhouLinkService.getPacsData(registrationOid);
		if(Func.isEmpty(pacsData)){
			return;
		}

		// 2. 构建HIS报告参数
		HisReportWebServiceParam param = buildReportData(pacsData);
		log.info("【sendReeportToHis】开始上传报告 registerOid:{},参数:{}", registrationOid,param);

		// 3. 设置请求参数 - 根据WSDL定义，参数名为as_json，类型为string
		//getSoapClient().setParam("as_json", JSON.toJSONString(param));
		SoapClient client = getSoapClient();
		client.setParam("as_json", JSON.toJSONString(param));

		// 调试：打印请求XML
		String requestXml = client.getMsgStr(true);
		log.info("【sendReeportToHis】上传报告 SOAP请求XML数据:\n{}", requestXml);

		// 4. 发送请求并获取响应（自动解析XML为字符串）
		String result = getSoapClient().send();
		log.info("【sendReeportToHis】上传报告 registerOid={}, 响应结果 result={}", registrationOid,result);

		// 5. 解析响应
		parseSoapResponse(result, registrationOid);
	}

	/**
	 * 解析SOAP响应
	 * @param response
	 * @param registrationOid
	 */
	private void parseSoapResponse(String response, Long registrationOid) {
		try {
			// 使用正则表达式快速判断响应类型
			if (response.contains("soap:Fault") || response.contains("<Fault>")) {
				// 解析Fault错误
				Matcher codeMatcher = FAULT_CODE_PATTERN.matcher(response);
				Matcher stringMatcher = FAULT_STRING_PATTERN.matcher(response);

				String faultCode = codeMatcher.find() ? codeMatcher.group(1).trim() : "UNKNOWN";
				String faultString = stringMatcher.find() ? stringMatcher.group(1).trim() : "未知错误";

				log.error("【sendReeportToHis】上传报告失败：registerOid={}, faultCode={}, faultString={}", registrationOid, faultCode, faultString);
				throw new ServiceException("【sendReeportToHis】上传报告失败：registerOid=" + registrationOid
					+ ", faultCode=" + faultCode + ", faultString=" + faultString);
			} else {
				// 解析成功响应
				Matcher resultMatcher = RESULT_PATTERN.matcher(response);
				if (resultMatcher.find()) {
					String jsonText = resultMatcher.group(1).trim();
					String cleanJsonText = jsonText
						.replace("“", "\"")
						.replace("”", "\"");

					// 解析JSON
					JSONArray jsonArray = JSON.parseArray(cleanJsonText);
					if (jsonArray != null && !jsonArray.isEmpty()) {
						Map<String, Object> result = jsonArray.getJSONObject(0);
						String status = (String) result.get("Status");
						if (status != null && status.toUpperCase().contains("SUCCESS")) {
							// 上传成功处理
							log.info("【sendReeportToHis】上传报告成功：registerOid={}, result={}", registrationOid, jsonText);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("解析SOAP响应失败：registerOid={}, errorMsg={}", registrationOid, e);
		}
	}

	private HisReportWebServiceParam buildReportData(PacsPatientExtVO pacsData) {
		HisReportWebServiceParam param = new HisReportWebServiceParam();
		param.setImg_application_id(pacsData.getApplication() != null ? pacsData.getApplication().getApplicationId() : "");
		param.setHis_patient_id(pacsData.getPacsPatient().getPatientId());
		param.setPatient_id(pacsData.getPacsPatient().getPatientId());
		param.setCase_id(pacsData.getApplication() != null ? pacsData.getApplication().getAdmissionNo() : "");
		param.setIn_hosp_id(pacsData.getApplication() != null ? pacsData.getApplication().getAdmissionNo() : "");
		param.setPatient_name(pacsData.getPacsPatient().getName());
		param.setPatient_sex(pacsData.getPacsPatient().getSex() == 1 ? "男" : "女");
		param.setStudy_date(DateUtil.formatDate(pacsData.getExam().getStudyTime()));
		param.setStudy_time(DateUtil.format(pacsData.getExam().getStudyTime(),"HH:mm:ss"));
		param.setReport_view(pacsData.getExamReport().getFindings());
		param.setReport_result(pacsData.getExamReport().getImpressions());
		param.setReport_doctor(pacsData.getExamReport().getReaderName() );
		param.setVerified_doctor(pacsData.getExamReport().getAuditByName());
		param.setBodypart(pacsData.getRegistration().getBodypart());
		param.setModality(pacsData.getRegistration().getModality());
		param.setReport_dttm(DateUtil.format(pacsData.getExamReport().getReportDate(),"yyyy-MM-dd HH:mm:ss"));
		param.setAffirm_status("1");
		param.setStudy_access_no(pacsData.getExam().getAccessionNumber());
		param.setStudy_instance_uid(pacsData.getExam().getStudyInstanceUid());
		param.setStudy_age(pacsData.getRegistration().getPatientAge());
		param.setLczd(pacsData.getRegistration().getDiagnosis());
		param.setOffice(pacsData.getRegistration().getApplicationDept());
		param.setBed_number(pacsData.getRegistration().getBedNo());
		param.setRefer_docotor(pacsData.getRegistration().getApplicationDoctor());
		param.setReference_file(pacsData.getExamReport().getReportFileName());
		param.setIs_positive(pacsData.getExamReport().getPositive() == 1 ? "是" : "否");
		return param;
	}

	public SoapClient getSoapClient() {
		if (soapClient == null) {
			// 1. 初始化SOAP客户端（指定WSDL地址）
			soapClient = SoapClient.create(hisReportWebserviceUrl)
				// 2. 设置要调用的方法名（需与WSDL中的方法一致） // 第二个参数是命名空间
				.setMethod(METHOD_NAME, NAME_SPACE_URL);
		}
		return soapClient;
	}
}
