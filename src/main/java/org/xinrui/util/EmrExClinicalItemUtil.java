package org.xinrui.util;

import java.util.HashMap;
import java.util.Map;

public class EmrExClinicalItemUtil {

    /**
     * 用于映射检查项目代码itemCode字段
     */
    private static final Map<String, String> MODALITY_CODE_MAP = new HashMap<>();
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
    private static final Map<String, String> MODALITY_NAME_MAP = new HashMap<>();
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
     */
    private static final Map<Integer, String> POSITIVE_CODE_MAP = new HashMap<>();
    static {
        //1为阳性，表示有异常,0为阴性，表示无异常。01为未见异常，02为异常，03为不确定
        POSITIVE_CODE_MAP.put(1,"02");
        POSITIVE_CODE_MAP.put(0,"01");
    }


    /**
     * 用于映射检查结果名称examinationResultName字段
     */
    private static final Map<Integer, String> POSITIVE_NAME_MAP = new HashMap<>();
    static {
        //1为阳性，为异常。0为阴性，为未见异常。其他为不确定
        POSITIVE_NAME_MAP.put(1,"异常");
        POSITIVE_NAME_MAP.put(0,"未见异常");
    }

    /**
     * 获取检查项目代码
     *
     * @param modality 检查类型标识
     * @return 项目代码（未找到时返回"99"）
     */
    public static String getItemCode(String modality) {
        return MODALITY_CODE_MAP.getOrDefault(modality, "99");
    }

    /**
     * 获取检查项目名称
     *
     * @param modality 检查类型标识
     * @return 项目名称（未找到时返回"其他"）
     */
    public static String getItemName(String modality) {
        return MODALITY_NAME_MAP.getOrDefault(modality, "其他");
    }

    /**
     * 获取检查结果代码
     * @param positive
     * @return 未找到时返回03（不确定）
     */
    public static String getExaminationResultCode(Integer positive){
        return POSITIVE_CODE_MAP.getOrDefault(positive,"03");
    }


    /**
     * 获取检查结果名称
     * @param positive
     * @return
     */
    public static String getExaminationResultName(Integer positive){
        return POSITIVE_NAME_MAP.getOrDefault(positive,"不确定");
    }
}
