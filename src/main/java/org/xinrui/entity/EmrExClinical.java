package org.xinrui.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/*
* 此类用于从T_Pacs_Exam_Report、T_Pacs_Registration、T_Pacs_Patient、T_Pacs_Application表中查询数据
* */

@Data
public class EmrExClinical {

	@ApiModelProperty(value = "检查报告OID")
	private Long oid;

	@ApiModelProperty(value = "机构内患者基本信息唯一标识")
	private String patientId;

	@ApiModelProperty(value = "患者来源类别")
	private String patientSourceType;

	@ApiModelProperty(value = "检查申请单号")
	private String applicationId;

	@ApiModelProperty(value = "患者姓名")
	private String name;

	@ApiModelProperty(value = "身份证号码")
	private String personId;

	@ApiModelProperty(value = "检查项目类别")
	private String modality;

	@ApiModelProperty(value = "检查报告id")
	private String reportId;

	@ApiModelProperty(value = "检查报告日期")
	private Date reportDate;

	@ApiModelProperty(value = "检查报告医生id")
	private Long reader;

	@ApiModelProperty(value = "检查机构id")
	private String tenantOid;

	@ApiModelProperty(value = "检查部门id")
	private String applicationDeptOid;


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

	public String getApplicationDeptOid() {
		return applicationDeptOid != null ? applicationDeptOid : "未知";
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
