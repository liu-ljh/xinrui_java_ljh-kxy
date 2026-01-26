package org.xinrui.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xinrui.entity.EmrExClinicalItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class EmrExClinicalItemMapperTest {

	@Autowired
	private EmrExClinicalItemMapper emrExClinicalItemMapper;

	@Test
	public void testSelectEmrExClinicalItem() {
		// 1. 准备查询参数
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = null;
		Date endTime = null;

		try {
			startTime = sdf.parse("2023-01-13");
			endTime = sdf.parse("2023-01-14");
		} catch (Exception e) {
			throw new RuntimeException("日期格式化失败", e);
		}

		// 2. 创建分页对象（第1页，每页10条）
		Page<EmrExClinicalItem> page = new Page<>(1, 10);

		// 3. 执行查询
		System.out.println("开始执行查询（时间范围: " + sdf.format(startTime) + " 至 " + sdf.format(endTime) + "）...");
		List<EmrExClinicalItem> result = emrExClinicalItemMapper.selectEmrExClinicalItem(page, startTime, endTime);

		// 4. 验证结果
		System.out.println("分页查询 - 总记录数: " + page.getTotal());
		System.out.println("分页查询 - 当前页记录数: " + result.size());

		if (result == null || result.isEmpty()) {
			if (page.getTotal() > 0) {
				System.err.println("警告: 数据库共有 " + page.getTotal() + " 条数据，但当前页未返回数据。");
				System.err.println("可能原因: 1. 实体类字段映射错误 2. 数据库时间范围不匹配");
			} else {
				System.out.println("未查询到数据。请检查数据库中是否有符合时间范围的数据。");
			}
		} else {
			System.out.println("查询成功！当前页返回 " + result.size() + " 条数据。");

			// 打印第一条数据的关键信息
			EmrExClinicalItem item = result.get(0);
			System.out.println("第一条数据 OID: " + item.getOid());
			System.out.println("第一条数据 报告ID: " + item.getReportId());
			System.out.println("第一条数据 检查类型: " + item.getModality());
			System.out.println("第一条数据 阳性结果: " + item.getPositive());
		}
	}
}
