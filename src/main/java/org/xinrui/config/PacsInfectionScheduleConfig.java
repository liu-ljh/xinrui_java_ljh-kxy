package org.xinrui.config;


/**
 * 传染病上报定时任务配置类
 */
public class PacsInfectionScheduleConfig {

    // 分页大小配置
    public static final int PAGE_SIZE = 50;

    // Redis键配置
    public static final String REDIS_INFECTION_REPORT_DATA = "baiyun2yuan-pacs-web:INFECTION_REPORT_DATA";
    public static final String REDIS_INFECTION_ITEM_DATA = "baiyun2yuan-pacs-web:INFECTION_ITEM_DATA";

    // 定时任务Cron表达式
    public static final String UPLOAD_CLINICAL_DATA_CRON = "0 59 23 * * ?";
    public static final String UPLOAD_CLINICAL_ITEM_DATA_CRON = "0 59 23 * * ?";
}

