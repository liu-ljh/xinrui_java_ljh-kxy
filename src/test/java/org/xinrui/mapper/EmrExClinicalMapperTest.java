package org.xinrui.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.entity.EmrExClinical;
import org.xinrui.mapper.EmrExClinicalMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest // 1. 标注为Spring Boot测试类，会启动Spring容器
public class EmrExClinicalMapperTest {

	@Autowired // 2. 自动注入你的Mapper
	private EmrExClinicalMapper emrExClinicalMapper;

	@Test
	public void testSelectEmrExClinical() {
		// 3. 准备查询参数
		Map<String, Date> queryMap = new HashMap<>();

		// 设置开始时间
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startTime = sdf.parse("2023-01-13");
			Date endTime = sdf.parse("2023-01-14");

			queryMap.put("startTime", startTime);
			queryMap.put("endTime", endTime);
		} catch (Exception e) {
			System.err.println("日期格式化失败: " + e.getMessage());
			return;
		}

		// 4. 准备分页参数
		Page<EmrExClinical> page = new Page<>(1, 10);

		// 5. 执行查询
		System.out.println("开始执行查询...");
		try {
			List<EmrExClinical> result = emrExClinicalMapper.selectEmrExClinical(page, queryMap);

			// 6. 验证结果
			// 重点：先检查 page 对象的总记录数，这比检查 result 列表更准确
			System.out.println("分页查询 - 总记录数: " + page.getTotal());
			System.out.println("分页查询 - 当前页记录数: " + result.size());

			if (result == null || result.isEmpty()) {
				if (page.getTotal() > 0) {
					System.err.println("警告: 数据库共有 " + page.getTotal() + " 条数据，但当前页未返回数据。");
					System.err.println("这可能是 MyBatis 结果集映射（ResultMap）配置错误导致的。");
				} else {
					System.out.println("未查询到数据。请检查数据库中是否有符合时间范围 (2023-01-13 到 2023-01-14) 的数据。");
				}
			} else {
				System.out.println("查询成功！当前页返回 " + result.size() + " 条数据。");

				// 打印第一条数据的关键信息
				EmrExClinical dto = result.get(0);
				System.out.println("第一条数据 ID: " + dto.getOid());
				System.out.println("第一条数据姓名: " + dto.getName());
			}
		} catch (org.springframework.dao.DataAccessException e) {
			// 捕获数据库访问异常
			System.err.println("查询过程中发生数据库访问异常！");
			System.err.println("异常类型: " + e.getClass().getName());

			// 打印最根本的 SQL 错误
			Throwable rootCause = e.getRootCause();
			if (rootCause != null) {
				System.err.println("根本原因: " + rootCause.getMessage());
			} else {
				System.err.println("错误详情: " + e.getMessage());
			}
		} catch (Exception e) {
			// 捕获其他未知异常
			System.err.println("查询过程中发生未知异常！");
			e.printStackTrace();
		}
	}




}
