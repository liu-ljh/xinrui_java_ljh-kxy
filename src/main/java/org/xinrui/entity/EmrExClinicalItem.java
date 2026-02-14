package org.xinrui.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 检查报告项目表数据
 * @author Administrator
 */
@Data
public class EmrExClinicalItem {

	@ApiModelProperty(value = "检查报告项目OID")
	private Long oid;

	@ApiModelProperty(value = "检查报告ID")
	private String reportId;

	@ApiModelProperty(value = "检查项目类别名称")
	private String modality;

	@ApiModelProperty(value = "检查结果代码")
	private Integer positive;

	@ApiModelProperty(value = "报告时间")
	private Date reportDate;

}
