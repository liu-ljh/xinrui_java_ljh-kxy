package org.xinrui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//检查报告部分（代码转文字/文字转代码）字段的转换工具类
public class EmrExClinicalUtil {

	// 门诊急诊字典映射
	private static final Map<String, String> ACTIVITY_TYPE_DICT = new HashMap<>();
	static {
		ACTIVITY_TYPE_DICT.put("1", "门诊");
		ACTIVITY_TYPE_DICT.put("2", "急诊");
		ACTIVITY_TYPE_DICT.put("3", "留观入观");
		ACTIVITY_TYPE_DICT.put("4", "留观出观");
		ACTIVITY_TYPE_DICT.put("5", "入院");
		ACTIVITY_TYPE_DICT.put("6", "住院");
		ACTIVITY_TYPE_DICT.put("7", "首次病程");
		ACTIVITY_TYPE_DICT.put("8", "日常病程");
		ACTIVITY_TYPE_DICT.put("9", "出院");
	}

	// 检查类别字典映射（映射为type_code）
	private static final Map<String, String> EXAMINATION_TYPE_CODE_DICT = new HashMap<>();
	static {
		EXAMINATION_TYPE_CODE_DICT.put("TCT", "11");
		EXAMINATION_TYPE_CODE_DICT.put("BD", "11");
		EXAMINATION_TYPE_CODE_DICT.put("XBX", "11");
		EXAMINATION_TYPE_CODE_DICT.put("IHC", "11");
		EXAMINATION_TYPE_CODE_DICT.put("CT", "10");
		EXAMINATION_TYPE_CODE_DICT.put("DR", "10");
		EXAMINATION_TYPE_CODE_DICT.put("DX", "10");
		EXAMINATION_TYPE_CODE_DICT.put("MR", "10");
		EXAMINATION_TYPE_CODE_DICT.put("CR", "10");
		EXAMINATION_TYPE_CODE_DICT.put("DSA", "10");
		EXAMINATION_TYPE_CODE_DICT.put("PET", "10");
		EXAMINATION_TYPE_CODE_DICT.put("US", "10");
		EXAMINATION_TYPE_CODE_DICT.put("特殊", "10");
		EXAMINATION_TYPE_CODE_DICT.put("XA", "10");
		EXAMINATION_TYPE_CODE_DICT.put("GES", "10");
		EXAMINATION_TYPE_CODE_DICT.put("CES", "10");
		EXAMINATION_TYPE_CODE_DICT.put("EES", "10");
		EXAMINATION_TYPE_CODE_DICT.put("BES", "10");
		EXAMINATION_TYPE_CODE_DICT.put("LES", "10");
		EXAMINATION_TYPE_CODE_DICT.put("HES", "10");
		EXAMINATION_TYPE_CODE_DICT.put("UES", "10");
	}

	// 检查类别字典映射（type_code转为type_name）
	private static final Map<String, String> EXAMINATION_TYPE_NAME_DICT = new HashMap<>();
	static {
		EXAMINATION_TYPE_NAME_DICT.put("10", "影像学检查");
		EXAMINATION_TYPE_NAME_DICT.put("11", "病理学检查");
		EXAMINATION_TYPE_NAME_DICT.put("99", "其他");

	}

	// 检查报告机构代码字典映射
	private static final Map<String, String> ORG_CODE_DICT = new HashMap<>();
	static {
        ORG_CODE_DICT.put("440111014", "广州市白云区第二人民医院");

	}

	// 科室代码字典映射（科室代码转为科室名称）
	private static final Map<String, String> DEPT_CODE_NAME_DICT = new HashMap<>();
	static {
		DEPT_CODE_NAME_DICT.put("A01", "预防保健科");
		DEPT_CODE_NAME_DICT.put("A02", "全科医疗科");
		DEPT_CODE_NAME_DICT.put("A03", "内科");
		DEPT_CODE_NAME_DICT.put("A03.01", "呼吸内科专业");
		DEPT_CODE_NAME_DICT.put("A03.02", "消化内科专业");
		DEPT_CODE_NAME_DICT.put("A03.03", "神经内科专业");
		DEPT_CODE_NAME_DICT.put("A03.04", "心血管内科专业");
		DEPT_CODE_NAME_DICT.put("A03.05", "血液内科专业");
		DEPT_CODE_NAME_DICT.put("A03.06", "肾病学专业");
		DEPT_CODE_NAME_DICT.put("A03.07", "内分泌专业");
		DEPT_CODE_NAME_DICT.put("A03.08", "免疫学专业");
		DEPT_CODE_NAME_DICT.put("A03.09", "变态反应专业");
		DEPT_CODE_NAME_DICT.put("A03.10", "老年病专业");
		DEPT_CODE_NAME_DICT.put("A03.11", "其他");
		DEPT_CODE_NAME_DICT.put("A04", "外科");
		DEPT_CODE_NAME_DICT.put("A04.01", "普通外科专业");
		DEPT_CODE_NAME_DICT.put("A04.01.01", "肝脏移植项目");
		DEPT_CODE_NAME_DICT.put("A04.01.02", "胰腺移植项目");
		DEPT_CODE_NAME_DICT.put("A04.01.03", "小肠移植项目");
		DEPT_CODE_NAME_DICT.put("A04.02", "神经外科专业");
		DEPT_CODE_NAME_DICT.put("A04.03", "骨科专业");
		DEPT_CODE_NAME_DICT.put("A04.04", "泌尿外科专业");
		DEPT_CODE_NAME_DICT.put("A04.04.01", "肾脏移植项目");
		DEPT_CODE_NAME_DICT.put("A04.05", "胸外科专业");
		DEPT_CODE_NAME_DICT.put("A04.05.01", "肺脏移植项目");
		DEPT_CODE_NAME_DICT.put("A04.06", "心脏大血管外科专业");
		DEPT_CODE_NAME_DICT.put("A04.06.01", "心脏移植项目");
		DEPT_CODE_NAME_DICT.put("A04.07", "烧伤科专业");
		DEPT_CODE_NAME_DICT.put("A04.08", "整形外科专业");
		DEPT_CODE_NAME_DICT.put("A04.09", "其他");
		DEPT_CODE_NAME_DICT.put("A05", "妇产科");
		DEPT_CODE_NAME_DICT.put("A05.01", "妇科专业");
		DEPT_CODE_NAME_DICT.put("A05.02", "产科专业");
		DEPT_CODE_NAME_DICT.put("A05.03", "计划生育专业");
		DEPT_CODE_NAME_DICT.put("A05.04", "优生学专业");
		DEPT_CODE_NAME_DICT.put("A05.05", "生殖健康与不孕症专业");
		DEPT_CODE_NAME_DICT.put("A05.06", "其他");
		DEPT_CODE_NAME_DICT.put("A06", "妇女保健科");
		DEPT_CODE_NAME_DICT.put("A06.01", "青春期保健专业");
		DEPT_CODE_NAME_DICT.put("A06.02", "围产期保健专业");
		DEPT_CODE_NAME_DICT.put("A06.03", "更年期保健专业");
		DEPT_CODE_NAME_DICT.put("A06.04", "妇女心理卫生专业");
		DEPT_CODE_NAME_DICT.put("A06.05", "妇女营养专业");
		DEPT_CODE_NAME_DICT.put("A06.06", "其他");
		DEPT_CODE_NAME_DICT.put("A07", "儿科");
		DEPT_CODE_NAME_DICT.put("A07.01", "新生儿专业");
		DEPT_CODE_NAME_DICT.put("A07.02", "小儿传染病专业");
		DEPT_CODE_NAME_DICT.put("A07.03", "小儿消化专业");
		DEPT_CODE_NAME_DICT.put("A07.04", "小儿呼吸专业");
		DEPT_CODE_NAME_DICT.put("A07.05", "小儿心脏病专业");
		DEPT_CODE_NAME_DICT.put("A07.06", "小儿肾病专业");
		DEPT_CODE_NAME_DICT.put("A07.07", "小儿血液病专业");
		DEPT_CODE_NAME_DICT.put("A07.08", "小儿神经病学专业");
		DEPT_CODE_NAME_DICT.put("A07.09", "小儿内分泌专业");
		DEPT_CODE_NAME_DICT.put("A07.10", "小儿遗传病专业");
		DEPT_CODE_NAME_DICT.put("A07.11", "小儿免疫专业");
		DEPT_CODE_NAME_DICT.put("A07.12", "其他");
		DEPT_CODE_NAME_DICT.put("A08", "小儿外科");
		DEPT_CODE_NAME_DICT.put("A08.01", "小儿普通外科专业");
		DEPT_CODE_NAME_DICT.put("A08.02", "小儿骨科专业");
		DEPT_CODE_NAME_DICT.put("A08.03", "小儿泌尿外科专业");
		DEPT_CODE_NAME_DICT.put("A08.04", "小儿胸心外科专业");
		DEPT_CODE_NAME_DICT.put("A08.05", "小儿神经外科专业");
		DEPT_CODE_NAME_DICT.put("A08.06", "其他");
		DEPT_CODE_NAME_DICT.put("A09", "儿童保健科");
		DEPT_CODE_NAME_DICT.put("A09.01", "儿童生长发育专业");
		DEPT_CODE_NAME_DICT.put("A09.02", "儿童营养专业");
		DEPT_CODE_NAME_DICT.put("A09.03", "儿童心理卫生专业");
		DEPT_CODE_NAME_DICT.put("A09.04", "儿童五官保健专业");
		DEPT_CODE_NAME_DICT.put("A09.05", "儿童康复专业");
		DEPT_CODE_NAME_DICT.put("A09.06", "其他");
		DEPT_CODE_NAME_DICT.put("A10", "眼科");
		DEPT_CODE_NAME_DICT.put("A11", "耳鼻咽喉科");
		DEPT_CODE_NAME_DICT.put("A11.01", "耳科专业");
		DEPT_CODE_NAME_DICT.put("A11.02", "鼻科专业");
		DEPT_CODE_NAME_DICT.put("A11.03", "咽喉科专业");
		DEPT_CODE_NAME_DICT.put("A11.04", "其他");
		DEPT_CODE_NAME_DICT.put("A12", "口腔科");
		DEPT_CODE_NAME_DICT.put("A12.01", "牙体牙髓病专业");
		DEPT_CODE_NAME_DICT.put("A12.02", "牙周病专业");
		DEPT_CODE_NAME_DICT.put("A12.03", "口腔黏膜病专业");
		DEPT_CODE_NAME_DICT.put("A12.04", "儿童口腔专业");
		DEPT_CODE_NAME_DICT.put("A12.05", "口腔颌骨外科专业");
		DEPT_CODE_NAME_DICT.put("A12.06", "口腔修复专业");
		DEPT_CODE_NAME_DICT.put("A12.07", "口腔正畸专业");
		DEPT_CODE_NAME_DICT.put("A12.08", "口腔种植专业");
		DEPT_CODE_NAME_DICT.put("A12.09", "口腔麻醉专业");
		DEPT_CODE_NAME_DICT.put("A12.10", "口腔颌面医学影像专业");
		DEPT_CODE_NAME_DICT.put("A13", "皮肤科");
		DEPT_CODE_NAME_DICT.put("A13.01", "皮肤病专业");
		DEPT_CODE_NAME_DICT.put("A13.02", "性传播疾病专业");
		DEPT_CODE_NAME_DICT.put("A13.03", "其他");
		DEPT_CODE_NAME_DICT.put("A14", "医疗美容科");
		DEPT_CODE_NAME_DICT.put("A15", "精神科");
		DEPT_CODE_NAME_DICT.put("A15.01", "精神病专业");
		DEPT_CODE_NAME_DICT.put("A15.02", "精神卫生专业");
		DEPT_CODE_NAME_DICT.put("A15.03", "药物依赖专业");
		DEPT_CODE_NAME_DICT.put("A15.04", "精神康复专业");
		DEPT_CODE_NAME_DICT.put("A15.05", "社区防治专业");
		DEPT_CODE_NAME_DICT.put("A15.06", "临床心理专业");
		DEPT_CODE_NAME_DICT.put("A15.07", "司法精神专业");
		DEPT_CODE_NAME_DICT.put("A15.08", "其他");
		DEPT_CODE_NAME_DICT.put("A16", "传染科");
		DEPT_CODE_NAME_DICT.put("A16.01", "肠道传染病专业");
		DEPT_CODE_NAME_DICT.put("A16.02", "呼吸道传染病专业");
		DEPT_CODE_NAME_DICT.put("A16.03", "肝炎专业");
		DEPT_CODE_NAME_DICT.put("A16.04", "虫媒传染病专业");
		DEPT_CODE_NAME_DICT.put("A16.05", "动物源性传染病专业");
		DEPT_CODE_NAME_DICT.put("A16.06", "蠕虫病专业");
		DEPT_CODE_NAME_DICT.put("A16.07", "其它");
		DEPT_CODE_NAME_DICT.put("A17", "结核病科");
		DEPT_CODE_NAME_DICT.put("A18", "地方病科");
		DEPT_CODE_NAME_DICT.put("A19", "肿瘤科");
		DEPT_CODE_NAME_DICT.put("A20", "急诊医学科");
		DEPT_CODE_NAME_DICT.put("A21", "康复医学科");
		DEPT_CODE_NAME_DICT.put("A22", "运动医学科");
		DEPT_CODE_NAME_DICT.put("A23", "职业病科");
		DEPT_CODE_NAME_DICT.put("A23.01", "职业中毒专业");
		DEPT_CODE_NAME_DICT.put("A23.02", "尘肺专业");
		DEPT_CODE_NAME_DICT.put("A23.03", "放射病专业");
		DEPT_CODE_NAME_DICT.put("A23.04", "物理因素损伤专业");
		DEPT_CODE_NAME_DICT.put("A23.05", "职业健康监护专业");
		DEPT_CODE_NAME_DICT.put("A23.06", "其他");
		DEPT_CODE_NAME_DICT.put("A24", "临终关怀科");
		DEPT_CODE_NAME_DICT.put("A25", "特种医学与军事医学科");
		DEPT_CODE_NAME_DICT.put("A26", "麻醉科");
		DEPT_CODE_NAME_DICT.put("A27", "疼痛科");
		DEPT_CODE_NAME_DICT.put("A28", "重症医学科");
		DEPT_CODE_NAME_DICT.put("A30", "医学检验科");
		DEPT_CODE_NAME_DICT.put("A30.01", "临床体液、血液专业");
		DEPT_CODE_NAME_DICT.put("A30.02", "临床微生物学专业");
		DEPT_CODE_NAME_DICT.put("A30.03", "临床生化检验专业");
		DEPT_CODE_NAME_DICT.put("A30.04", "临床免疫、血清学专业");
		DEPT_CODE_NAME_DICT.put("A30.05", "临床细胞分子遗传学专业");
		DEPT_CODE_NAME_DICT.put("A30.06", "其他");
		DEPT_CODE_NAME_DICT.put("A31", "病理科");
		DEPT_CODE_NAME_DICT.put("A32", "医学影像科");
		DEPT_CODE_NAME_DICT.put("A32.01", "X线诊断专业");
		DEPT_CODE_NAME_DICT.put("A32.02", "CT诊断专业");
		DEPT_CODE_NAME_DICT.put("A32.03", "磁共振成像诊断专业");
		DEPT_CODE_NAME_DICT.put("A32.04", "核医学专业");
		DEPT_CODE_NAME_DICT.put("A32.05", "超声诊断专业");
		DEPT_CODE_NAME_DICT.put("A32.06", "心电诊断专业");
		DEPT_CODE_NAME_DICT.put("A32.07", "脑电及脑血流图诊断专业");
		DEPT_CODE_NAME_DICT.put("A32.08", "神经肌肉电图专业");
		DEPT_CODE_NAME_DICT.put("A32.09", "介入放射学专业");
		DEPT_CODE_NAME_DICT.put("A32.10", "放射治疗专业");
		DEPT_CODE_NAME_DICT.put("A32.11", "其他");
		DEPT_CODE_NAME_DICT.put("A50", "中医科");
		DEPT_CODE_NAME_DICT.put("A50.01", "内科专业");
		DEPT_CODE_NAME_DICT.put("A50.02", "外科专业");
		DEPT_CODE_NAME_DICT.put("A50.03", "妇产科专业");
		DEPT_CODE_NAME_DICT.put("A50.04", "儿科专业");
		DEPT_CODE_NAME_DICT.put("A50.05", "皮肤科专业");
		DEPT_CODE_NAME_DICT.put("A50.06", "眼科专业");
		DEPT_CODE_NAME_DICT.put("A50.07", "耳鼻咽喉科专业");
		DEPT_CODE_NAME_DICT.put("A50.08", "口腔科专业");
		DEPT_CODE_NAME_DICT.put("A50.09", "肿瘤科专业");
		DEPT_CODE_NAME_DICT.put("A50.10", "骨伤科专业");
		DEPT_CODE_NAME_DICT.put("A50.11", "肛肠科专业");
		DEPT_CODE_NAME_DICT.put("A50.12", "老年病科专业");
		DEPT_CODE_NAME_DICT.put("A50.13", "针灸科专业");
		DEPT_CODE_NAME_DICT.put("A50.14", "推拿科专业");
		DEPT_CODE_NAME_DICT.put("A50.15", "康复医学专业");
		DEPT_CODE_NAME_DICT.put("A50.16", "急诊科专业");
		DEPT_CODE_NAME_DICT.put("A50.17", "预防保健科专业");
		DEPT_CODE_NAME_DICT.put("A50.18", "其他");
		DEPT_CODE_NAME_DICT.put("A51", "民族医学科");
		DEPT_CODE_NAME_DICT.put("A51.01", "维吾尔医学");
		DEPT_CODE_NAME_DICT.put("A51.02", "藏医学");
		DEPT_CODE_NAME_DICT.put("A51.03", "蒙医学");
		DEPT_CODE_NAME_DICT.put("A51.04", "彝医学");
		DEPT_CODE_NAME_DICT.put("A51.05", "傣医学");
		DEPT_CODE_NAME_DICT.put("A51.06", "其他");
		DEPT_CODE_NAME_DICT.put("A52", "中西医结合科");
		DEPT_CODE_NAME_DICT.put("A53", "发热门诊（诊室）");
		DEPT_CODE_NAME_DICT.put("A69", "其他业务科室");
		DEPT_CODE_NAME_DICT.put("B01", "传染病预防控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B02", "性病艾滋病预防控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B03", "结核病预防控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B04", "血吸虫预防控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B05", "慢性非传染性疾病预防控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B06", "寄生虫病预防控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B07", "地方病控制科（中心）");
		DEPT_CODE_NAME_DICT.put("B08", "精神卫生科（中心）");
		DEPT_CODE_NAME_DICT.put("B09", "妇幼保健科");
		DEPT_CODE_NAME_DICT.put("B10", "免疫规划科（中心）");
		DEPT_CODE_NAME_DICT.put("B11", "农村改水技术指导科（中心）");
		DEPT_CODE_NAME_DICT.put("B12", "疾病控制与应急处理办公室");
		DEPT_CODE_NAME_DICT.put("B13", "食品卫生科");
		DEPT_CODE_NAME_DICT.put("B14", "环境卫生所");
		DEPT_CODE_NAME_DICT.put("B15", "职业卫生科");
		DEPT_CODE_NAME_DICT.put("B16", "放射卫生科");
		DEPT_CODE_NAME_DICT.put("B17", "学校卫生科");
		DEPT_CODE_NAME_DICT.put("B18", "健康教育科(中心)");
		DEPT_CODE_NAME_DICT.put("B19", "预防医学门诊");
		DEPT_CODE_NAME_DICT.put("B69", "其他业务科室");
		DEPT_CODE_NAME_DICT.put("C01", "综合卫生监督科");
		DEPT_CODE_NAME_DICT.put("C02", "产品卫生监督科");
		DEPT_CODE_NAME_DICT.put("C03", "职业卫生监督科");
		DEPT_CODE_NAME_DICT.put("C04", "环境卫生监督科");
		DEPT_CODE_NAME_DICT.put("C05", "传染病执法监督科");
		DEPT_CODE_NAME_DICT.put("C06", "医疗服务监督科");
		DEPT_CODE_NAME_DICT.put("C07", "稽查科(大队)");
		DEPT_CODE_NAME_DICT.put("C08", "许可受理科");
		DEPT_CODE_NAME_DICT.put("C09", "放射卫生监督科");
		DEPT_CODE_NAME_DICT.put("C10", "学校卫生监督科");
		DEPT_CODE_NAME_DICT.put("C11", "食品安全监督科");
		DEPT_CODE_NAME_DICT.put("C69", "其他");
		DEPT_CODE_NAME_DICT.put("D71", "护理部");
		DEPT_CODE_NAME_DICT.put("D72", "药剂科(药房)");
		DEPT_CODE_NAME_DICT.put("D73", "感染科");
		DEPT_CODE_NAME_DICT.put("D74", "输血科(血库)");
		DEPT_CODE_NAME_DICT.put("D81", "办公室");
		DEPT_CODE_NAME_DICT.put("D82", "人事科");
		DEPT_CODE_NAME_DICT.put("D83", "财务科");
		DEPT_CODE_NAME_DICT.put("D84", "设备科");
		DEPT_CODE_NAME_DICT.put("D85", "信息科(中心)");
		DEPT_CODE_NAME_DICT.put("D86", "医政科");
		DEPT_CODE_NAME_DICT.put("D87", "教育培训科");
		DEPT_CODE_NAME_DICT.put("D88", "总务科");
		DEPT_CODE_NAME_DICT.put("D89", "新农合管理办公室");
		DEPT_CODE_NAME_DICT.put("D99", "其他科室");
	}

	public static String convertActivityType(String activityTypeCode) {
		return convert(ACTIVITY_TYPE_DICT, activityTypeCode);
	}

	public static String convertExaminationTypeCode(String examinationTypeDesc){
		return convert(EXAMINATION_TYPE_CODE_DICT,examinationTypeDesc);
	}

	public static String convertExaminationTypeName(String examinationTypeDesc) {
		String typeCode = convert(EXAMINATION_TYPE_CODE_DICT, examinationTypeDesc);
		if (typeCode == null || "未能正确处理，得到结果为空值".equals(typeCode)) {
			return "其他";
		}
		return EXAMINATION_TYPE_NAME_DICT.getOrDefault(typeCode, "其他");
	}

	public static String convertDeptCode(String deptCode){
		return convert(DEPT_CODE_NAME_DICT,deptCode);
	}

	public static String convertOrgCode(String orgCode){
		return convert(ORG_CODE_DICT,orgCode);
	}

	private static String convert(Map<String, String> dictMap, String code) {
		if (code == null || code.isEmpty()) {
			return null;
		}
		String result = dictMap.get(code);
		return result != null ? result : "未能正确处理，得到结果为空值";
	}

	public static String idCardTypeCodeJudge(String idCard){

	    if (idCard == null || idCard.isEmpty()) {
            return null;
        }
        else if (idCard.length() == 18) {
        	return "01";
		}else{
        	return "证件号码非18位，证件为非居民身份证";
		}
	}

	public static String idCardTypeNameJudge(String idCard){
		if (idCard == null || idCard.isEmpty()) {
			return null;
		}
		else if (idCard.length() == 18) {
			return "居民身份证";
		}else{
			return "证件号码非18位，证件为非居民身份证";
		}

	}

	// 定义数据库中的日期格式
	private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 将 Report_Date 从 Date 类型转换为 String 类型
	 * 格式保持与数据库一致：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static String convertReportDate(Date reportDate) {
		if (reportDate == null) {
			return "未知"; // 或者 return ""; 根据业务需求
		}

		SimpleDateFormat sdf = new SimpleDateFormat(DB_DATE_FORMAT);
		return sdf.format(reportDate);
	}

}
