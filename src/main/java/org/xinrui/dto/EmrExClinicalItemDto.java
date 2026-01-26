package org.xinrui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 检查报告项目
 * 此表用于接收检查详细结果信息
 * @author Administrator
 */
@Data
public class EmrExClinicalItemDto {

	@ApiModelProperty(value = "ID")
	private String id;

	@ApiModelProperty(value = "检查报告ID")
	private String exClinicalId;

	@ApiModelProperty(value = "检查项目代码")
	private String itemCode;

	@ApiModelProperty(value = "检查项目名称")
	private String itemName;

	@ApiModelProperty(value = "检查结果代码：01 02 03（01为未见异常，02为异常，03为不确定）")
	private String examinationResultCode;

	@ApiModelProperty(value = "检查结果名称：未见异常 异常 不确定")
	private String examinationResultName;


	@ApiModelProperty(value = "操作时间，精确到秒，格式为yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date operationTime;
}
