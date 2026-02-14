package org.xinrui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 检查报告表参数DTO
 * 此表用于调用检查报告项目表数据操作 API 接口
 * @author Administrator
 */
@Data
public class EmrExClinicalDto {

	@ApiModelProperty(value = "检查信息在院内的唯一识别标识")
	private String id;

	@JsonProperty("patient_id")
	@ApiModelProperty(value = "机构内患者基本信息唯一标识")
	private String patientId;

	@JsonProperty("activity_type_code")
	@ApiModelProperty(value = "关联的就诊活动类型代码")
	private String activityTypeCode;

	@JsonProperty("activity_type_name")
	@ApiModelProperty(value = "关联的就诊活动类型名称")
	private String activityTypeName;

	@JsonProperty("serial_number")
	@ApiModelProperty(value = "就诊流水号，门诊为门急诊号，住院为住院号")
	private String serialNumber;

	@JsonProperty("patient_name")
	@ApiModelProperty(value = "患者本人在公安户籍管理部门正式登记注册的姓氏和名称")
	private String patientName;

	@JsonProperty("id_card_type_code")
	@ApiModelProperty(value = "患者身份证件所属类别代码")
	private String idCardTypeCode;

	@JsonProperty("id_card_type_name")
	@ApiModelProperty(value = "患者身份证件所属类别名称")
	private String idCardTypeName;

	@JsonProperty("id_card")
	@ApiModelProperty(value = "患者的身份证件上的唯一法定标识符。新生儿无身份证件号码时，身份证件类别应为其他法定有效证件，身份证件号码为出生日期8位数字")
	private String idCard;

	@JsonProperty("examination_type_code")
	@ApiModelProperty(value = "患者检查报告所属类别代码")
	private String examinationTypeCode;

	@JsonProperty("examination_type_name")
	@ApiModelProperty(value = "患者检查报告所属类别名称")
	private String examinationTypeName;

	@JsonProperty("examination_objective_desc")
	@ApiModelProperty(value = "检查报告结果结果-客观所见")
	private String examinationObjectiveDesc;

	@JsonProperty("examination_subjective_desc")
	@ApiModelProperty(value = "检查报告结果-主观提示")
	private String examinationSubjectiveDesc;

	@JsonProperty("examination_report_no")
	@ApiModelProperty(value = "按照某一特定编码规则赋予检查报告单的顺序号")
	private String examinationReportNo;

	@JsonProperty("examination_report_date")
	@ApiModelProperty(value = "检查报告当日的公元纪年日期的完整描述")
	private Date examinationReportDate;

	@JsonProperty("examination_report_id")
	@ApiModelProperty(value = "报告医师的在医院信息系统用户信息表中的“用户 ID”")
	private String examinationReportId;

	@JsonProperty("org_code")
	@ApiModelProperty(value = "检查报告机构代码")
	private String orgCode;

	@JsonProperty("org_name")
	@ApiModelProperty(value = "检查报告机构名称")
	private String orgName;

	@JsonProperty("dept_code")
	@ApiModelProperty(value = "检查报告科室代码")
	private String deptCode;

	@JsonProperty("dept_name")
	@ApiModelProperty(value = "检查报告科室名称")
	private String deptName;

	@JsonProperty("operation_time")
	@ApiModelProperty(value = "数据操作的具体时间，精确到秒，格式为 yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date operationTime;

//	// ================== 以下为自定义判空getter ==================
//	public String getId() {
//		return id != null ? id : "未知";
//	}
//
//	public String getPatientId() {
//		return patientId != null ? patientId : "未知";
//	}
//
//	public String getActivityTypeCode() {
//		return activityTypeCode != null ? activityTypeCode : "未知";
//	}
//
//	public String getActivityTypeName() {
//		return activityTypeName != null ? activityTypeName : "未知";
//	}
//
//	public String getSerialNumber() {
//		return serialNumber != null ? serialNumber : "未知";
//	}
//
//	public String getPatientName() {
//		return patientName != null ? patientName : "未知";
//	}
//
//	public String getIdCardTypeCode() {
//		return idCardTypeCode != null ? idCardTypeCode : "未知";
//	}
//
//	public String getIdCardTypeName() {
//		return idCardTypeName != null ? idCardTypeName : "未知";
//	}
//
//	public String getIdCard() {
//		return idCard != null ? idCard : "未知";
//	}
//
//	public String getExaminationTypeCode() {
//		return examinationTypeCode != null ? examinationTypeCode : "未知";
//	}
//
//	public String getExaminationTypeName() {
//		return examinationTypeName != null ? examinationTypeName : "未知";
//	}
//
//	public String getExaminationReportNo() {
//		return examinationReportNo != null ? examinationReportNo : "未知";
//	}
//
//	public String getExaminationReportId() {
//		return examinationReportId != null ? examinationReportId : "未知";
//	}
//
//	public String getOrgCode() {
//		return orgCode != null ? orgCode : "未知";
//	}
//
//	public String getOrgName() {
//		return orgName != null ? orgName : "未知";
//	}
//
//	public String getDeptCode() {
//		return deptCode != null ? deptCode : "未知";
//	}
//
//	public String getDeptName() {
//		return deptName != null ? deptName : "未知";
//	}
//	// ================== 判空getter结束 ==================
}
