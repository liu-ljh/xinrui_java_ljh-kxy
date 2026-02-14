package org.xinrui.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 检查报告表数据
 * @author Administrator
 */
@Data
public class EmrExClinical {

	@ApiModelProperty(value = "检查报告OID")
	private Long oid;

	@ApiModelProperty(value = "机构内患者基本信息唯一标识")
	private String patientId;

	@ApiModelProperty(value = "患者来源类别")
	private String patientSourceType;

	@ApiModelProperty(value = "门诊号")
	private String outpatientNo;

	@ApiModelProperty(value = "住院号")
	private String admissionNo;

	@ApiModelProperty(value = "患者姓名")
	private String name;

	@ApiModelProperty(value = "身份证号码")
	private String personId;

	@ApiModelProperty(value = "床号")
	private String bedNo;

	@ApiModelProperty(value = "检查申请单号")
	private String applicationId;

	@ApiModelProperty(value = "申请检查科室")
	private String applicationDeptName;

	@ApiModelProperty(value = "检查项目类别")
	private String modality;

	@ApiModelProperty(value = "所见")
	private String findings;

	@ApiModelProperty(value = "结论")
	private String impressions;

	@ApiModelProperty(value = "检查报告id")
	private String reportId;

	@ApiModelProperty(value = "检查报告日期")
	private Date reportDate;

	@ApiModelProperty(value = "检查报告医生id")
	private Long reader;

	@ApiModelProperty(value = "检查机构id")
	private String tenantOid;


	// 为所有字符串类型属性添加判空处理
	public String getPatientId() {
		return patientId != null ? patientId : "未知";
	}

	public String getPatientSourceType() {
		return patientSourceType != null ? patientSourceType : "未知";
	}

	public String getApplicationId() {
		return applicationId != null ? applicationId : "未知";
	}

	public String getName() {
		return name != null ? name : "未知";
	}

	public String getPersonId() {
		return personId != null ? personId : "未知";
	}

	public String getModality() {
		return modality != null ? modality : "未知";
	}

	public String getReportId() {
		return reportId != null ? reportId : "未知";
	}

	public String getTenantOid() {
		return tenantOid != null ? tenantOid : "未知";
	}

	public String getApplicationDeptName() {
		return applicationDeptName != null ? applicationDeptName : "未知";
	}

	// 非字符串类型属性保持原始逻辑（不进行判空）
	public Long getOid() {
		return oid;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public Long getReader() {
		return reader;
	}

}
