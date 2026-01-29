package org.xinrui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于映射EmrExClinicalItemDto数据库查出来字段与文档所需字段对应关系
 */
public class ModalityMapping {

	public static class ModalityInfo {
		private final String itemCode;
		private final String itemName;

		public ModalityInfo(String itemCode, String itemName) {
			this.itemCode = itemCode;
			this.itemName = itemName;
		}

		public String getItemCode() {
			return itemCode;
		}

		public String getItemName() {
			return itemName;
		}
	}


	private static final Map<String, ModalityInfo> MODALITY_MAP = new HashMap<>();


	static {
		MODALITY_MAP.put("CT", new ModalityInfo("1005", "计算机断层扫描（CT）"));
		MODALITY_MAP.put("DR", new ModalityInfo("1003", "X线检查"));
		MODALITY_MAP.put("DX", new ModalityInfo("1003", "X线检查"));
		MODALITY_MAP.put("MR", new ModalityInfo("1001", "磁共振"));
		MODALITY_MAP.put("CR", new ModalityInfo("1003", "X线检查"));
		MODALITY_MAP.put("DSA", new ModalityInfo("1003", "X线检查"));
		MODALITY_MAP.put("PET", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("US", new ModalityInfo("1002", "B超扫描"));
		MODALITY_MAP.put("GES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("CES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("EES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("BES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("LES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("HES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("UES", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("特殊", new ModalityInfo("99", "其他"));
		MODALITY_MAP.put("XA", new ModalityInfo("1003", "X线检查"));
		MODALITY_MAP.put("TCT", new ModalityInfo("1102", "细胞病理学"));
		MODALITY_MAP.put("GMP", new ModalityInfo("1101", "组织病理学"));
		MODALITY_MAP.put("XBX", new ModalityInfo("1102", "细胞病理学"));
		MODALITY_MAP.put("BD", new ModalityInfo("1101", "组织病理学"));
		MODALITY_MAP.put("IHC", new ModalityInfo("1101", "组织病理学"));
	}


	public static String getItemCode(String modality) {
		ModalityInfo info = MODALITY_MAP.get(modality);
		return info != null ? info.getItemCode() : "99"; // 默认值：“99-其他”
	}


	public static String getItemName(String modality) {
		ModalityInfo info = MODALITY_MAP.get(modality);
		return info != null ? info.getItemName() : "其他"; // 默认值：“其他”
	}
}
