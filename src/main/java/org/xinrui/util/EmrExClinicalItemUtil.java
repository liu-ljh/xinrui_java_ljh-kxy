package org.xinrui.util;

import org.xinrui.constant.EmrExClinicalItemDictConstants;

/**
 * EMR(电子病历)检查项目相关的工具类
 * 提供检查项目代码、名称和检查结果代码、名称的转换方法
 */
public class EmrExClinicalItemUtil {

	private EmrExClinicalItemUtil() {} // 私有构造方法，防止实例化

	/**
	 * 获取检查项目代码
	 *
	 * @param modality 检查类型标识
	 * @return 项目代码（未找到时返回"99"）
	 */
	public static String getItemCode(String modality) {
		return EmrExClinicalItemDictConstants.MODALITY_CODE_MAP.getOrDefault(modality, "99");
	}

	/**
	 * 获取检查项目名称
	 *
	 * @param modality 检查类型标识
	 * @return 项目名称（未找到时返回"其他"）
	 */
	public static String getItemName(String modality) {
		return EmrExClinicalItemDictConstants.MODALITY_NAME_MAP.getOrDefault(modality, "其他");
	}

	/**
	 * 获取检查结果代码
	 * @param positive
	 * @return 未找到时返回03（不确定）
	 */
	public static String getExaminationResultCode(Integer positive) {
		return EmrExClinicalItemDictConstants.POSITIVE_CODE_MAP.getOrDefault(positive, "03");
	}

	/**
	 * 获取检查结果名称
	 * @param positive
	 * @return 未找到时返回"不确定"
	 */
	public static String getExaminationResultName(Integer positive) {
		return EmrExClinicalItemDictConstants.POSITIVE_NAME_MAP.getOrDefault(positive, "不确定");
	}
}
