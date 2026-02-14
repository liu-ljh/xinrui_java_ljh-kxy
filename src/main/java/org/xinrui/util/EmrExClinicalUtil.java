package org.xinrui.util;

import org.xinrui.constant.EmrExClinicalDictConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * EMR(电子病历)临床相关的工具类
 * 提供各种转换和判断方法
 * @author Administrator
 */
public class EmrExClinicalUtil {

	private EmrExClinicalUtil() {} // 私有构造方法，防止实例化

	public static String convertActivityType(String activityTypeCode) {
		return convert(EmrExClinicalDictConstants.ACTIVITY_TYPE_DICT, activityTypeCode);
	}

	public static String convertExaminationTypeCode(String examinationTypeDesc){
		return convert(EmrExClinicalDictConstants.EXAMINATION_TYPE_CODE_DICT, examinationTypeDesc);
	}

	public static String convertExaminationTypeName(String examinationTypeDesc) {
		String typeCode = convert(EmrExClinicalDictConstants.EXAMINATION_TYPE_CODE_DICT, examinationTypeDesc);
		if (typeCode == null || "未能正确处理，得到结果为空值".equals(typeCode)) {
			return "其他";
		}
		return EmrExClinicalDictConstants.EXAMINATION_TYPE_NAME_DICT.getOrDefault(typeCode, "其他");
	}

	public static String convertDeptCode(String deptCode){
		return convert(EmrExClinicalDictConstants.DEPT_CODE_NAME_DICT, deptCode);
	}

	public static String getDeptNameByCode(String deptName){
		for (Map.Entry<String, String> entry : EmrExClinicalDictConstants.DEPT_CODE_NAME_DICT.entrySet()) {
			if (entry.getValue().equals(deptName)) {
				return entry.getKey();
			}
		}
		return "D99";
	}

	public static String convertOrgCode(String orgCode){
		return convert(EmrExClinicalDictConstants.ORG_CODE_DICT, orgCode);
	}

	private static String convert(Map<String, String> dictMap, String code) {
		if (code == null || code.isEmpty()) {
			return null;
		}
		String result = dictMap.get(code);
		return result != null ? result : "未知";
	}

	public static String idCardTypeCodeJudge(String idCard){
		if (idCard == null || idCard.isEmpty()) {
			return null;
		}
		else if (idCard.length() == 18) {
			return "01";
		} else {
			return "证件号码非18位，证件为非居民身份证";
		}
	}

	public static String idCardTypeNameJudge(String idCard){
		if (idCard == null || idCard.isEmpty()) {
			return null;
		}
		else if (idCard.length() == 18) {
			return "居民身份证";
		} else {
			return "证件号码非18位，证件为非居民身份证";
		}
	}

	// 定义数据库中的日期格式
	private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 将 Report_Date 从 Date 类型转换为 String 类型
	 * 格式保持与数据库一致：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static String convertReportDate(Date reportDate) {
		if (reportDate == null) {
			return "未知";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DB_DATE_FORMAT);
		return sdf.format(reportDate);
	}
}
