package org.xinrui.dto;

import java.io.Serializable;


public class ResultDTO implements Serializable {

	private static final long serialVersionUID = 1L; // 序列化版本号

	/** 操作结果（true=成功，false=失败）*/
	private Boolean result;

	/** 操作描述信息 */
	private String desc;

	private String id;

	/** 错误码（失败时必填）*/
	private String errorCode;

	/** 错误名称（失败时必填）*/
	private String errorName;


	public ResultDTO(){

	}

	/** 成功响应构造器 */
	public ResultDTO(Boolean result, String desc, String id) {
		this.result = result;
		this.desc = desc;
		this.id = id;
	}

	/** 失败响应构造器 */
	public ResultDTO(Boolean result, String errorCode, String errorName, String desc, String id) {
		this.result = result;
		this.errorCode = errorCode;
		this.errorName = errorName;
		this.desc = desc;
		this.id = id;
	}

	/** 创建成功响应 */
	public static ResultDTO success(String desc, String id) {
		return new ResultDTO(true, desc, id);
	}

	/** 创建失败响应 */
	public static ResultDTO error(String errorCode, String errorName, String desc, String id) {
		return new ResultDTO(false, errorCode, errorName, desc, id);
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}
}
