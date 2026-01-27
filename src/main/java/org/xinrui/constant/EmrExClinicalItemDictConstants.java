package org.xinrui.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * EMR(电子病历)检查项目相关的字典常量类
 * 包含检查项目代码、名称和检查结果代码、名称的映射关系
 */
public class EmrExClinicalItemDictConstants {

	// 私有构造方法，防止实例化
	private EmrExClinicalItemDictConstants() {}

	/**
	 * 用于映射检查项目代码itemCode字段
	 */
	public static final Map<String, String> MODALITY_CODE_MAP = new HashMap<>();
	static {
		MODALITY_CODE_MAP.put("CT", "1005");
		MODALITY_CODE_MAP.put("DR", "1003");
		MODALITY_CODE_MAP.put("DX", "1003");
		MODALITY_CODE_MAP.put("MR", "1001");
		MODALITY_CODE_MAP.put("CR", "1003");
		MODALITY_CODE_MAP.put("DSA", "1003");
		MODALITY_CODE_MAP.put("PET", "99");
		MODALITY_CODE_MAP.put("US", "1002");
		MODALITY_CODE_MAP.put("GES", "99");
		MODALITY_CODE_MAP.put("CES", "99");
		MODALITY_CODE_MAP.put("EES", "99");
		MODALITY_CODE_MAP.put("BES", "99");
		MODALITY_CODE_MAP.put("LES", "99");
		MODALITY_CODE_MAP.put("HES", "99");
		MODALITY_CODE_MAP.put("UES", "99");
		MODALITY_CODE_MAP.put("特殊", "99");
		MODALITY_CODE_MAP.put("XA", "1003");
		MODALITY_CODE_MAP.put("TCT", "1102");
		MODALITY_CODE_MAP.put("GMP", "1101");
		MODALITY_CODE_MAP.put("XBX", "1102");
		MODALITY_CODE_MAP.put("BD", "1101");
		MODALITY_CODE_MAP.put("IHC", "1101");
	}

	/**
	 * 用于映射检查项目名称itemName字段
	 */
	public static final Map<String, String> MODALITY_NAME_MAP = new HashMap<>();
	static {
		MODALITY_NAME_MAP.put("CT", "计算机断层扫描（CT）");
		MODALITY_NAME_MAP.put("DR", "X线检查");
		MODALITY_NAME_MAP.put("DX", "X线检查");
		MODALITY_NAME_MAP.put("MR", "磁共振");
		MODALITY_NAME_MAP.put("CR", "X线检查");
		MODALITY_NAME_MAP.put("DSA", "X线检查");
		MODALITY_NAME_MAP.put("PET", "其他");
		MODALITY_NAME_MAP.put("US", "B超扫描");
		MODALITY_NAME_MAP.put("GES", "其他");
		MODALITY_NAME_MAP.put("CES", "其他");
		MODALITY_NAME_MAP.put("EES", "其他");
		MODALITY_NAME_MAP.put("BES", "其他");
		MODALITY_NAME_MAP.put("LES", "其他");
		MODALITY_NAME_MAP.put("HES", "其他");
		MODALITY_NAME_MAP.put("UES", "其他");
		MODALITY_NAME_MAP.put("特殊", "其他");
		MODALITY_NAME_MAP.put("XA", "X线检查");
		MODALITY_NAME_MAP.put("TCT", "细胞病理学");
		MODALITY_NAME_MAP.put("GMP", "组织病理学");
		MODALITY_NAME_MAP.put("XBX", "细胞病理学");
		MODALITY_NAME_MAP.put("BD", "组织病理学");
		MODALITY_NAME_MAP.put("IHC", "组织病理学");
	}

	/**
	 * 用于映射检查结果代码examinationResultCode字段
	 * 1为阳性，表示有异常,0为阴性，表示无异常。01为未见异常，02为异常，03为不确定
	 */
	public static final Map<Integer, String> POSITIVE_CODE_MAP = new HashMap<>();
	static {
		POSITIVE_CODE_MAP.put(1, "02");
		POSITIVE_CODE_MAP.put(0, "01");
	}

	/**
	 * 用于映射检查结果名称examinationResultName字段
	 * 1为阳性，为异常。0为阴性，为未见异常。其他为不确定
	 */
	public static final Map<Integer, String> POSITIVE_NAME_MAP = new HashMap<>();
	static {
		POSITIVE_NAME_MAP.put(1, "异常");
		POSITIVE_NAME_MAP.put(0, "未见异常");
	}
}
