package org.xinrui.service;

/**
 * 传染病上报服务
 * @author Administrator
 */
public interface IPacsIdissService {

	/**
	 * 上报检查报告数据
	 */
	void uploadEmrExClinicalData();

	/**
	 * 上报检查报告项目数据
	 */
	void uploadEmrExClinicalItemData();

	/**
	 * 清空Redis缓存数据
	 */
	void clearRedis();
}
