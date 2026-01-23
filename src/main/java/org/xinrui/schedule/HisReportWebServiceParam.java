package org.xinrui.schedule;

import lombok.Data;

@Data
public class HisReportWebServiceParam {
/**  HIS 申请单ID  */
 public String img_application_id;
 /**        HIS 病人ID        字符串20位       */
 public String his_patient_id;
 /**        病人ID        字符串20位           */
 public String patient_id;
 /**        病案ID        字符串32位     */
 public String case_id;
 /**        住院ID        字符串20位  */
 public String in_hosp_id;
 /**        姓名        字符串32位   */
 public String patient_name;
 /**        性别        字符串1位 */
 public String patient_sex;
 /**        检查日期        字符串20位 */
 public String study_date;
 /**        检查时间        字符串20位 */
 public String study_time;
 /**        图像所见        字符串2048位变长   */
 public String report_view;
 /**        检查结果        字符串2048位变长  */
 public String report_result;
 /**        报告医生姓名        字符串32位  */
 public String report_doctor;
 /**        审核医生姓名        字符串32位    */
 public String verified_doctor;
 /**        部位        字符串200位   */
 public String bodypart;
 /**        设备类型        字符串8位   */
 public String modality;
 /**        报告日期        字符串18位     */
 public String report_dttm;
 /**        确认状态        字符串1位   */
 public String affirm_status;
 /**        检查号        字符串32位    */
 public String study_access_no;
 /**        检查实例ID        字符串32位    */
 public String study_instance_uid;
 /**        检查年龄        字符串8位     */
 public String study_age;
 /**        临床诊断        字符串60位  */
 public String lczd;
 /**        申请科室        字符串40位  */
 public String office;
 /**        住院床号        字符串10位  */
 public String bed_number;
 /**        申请医生        字符串32位   */
 public String refer_docotor;
 /**        申请文档名称        字符串256位      */
 public String reference_file;
 /**add by michael 2020-09-05   */
 public String is_positive;
}
