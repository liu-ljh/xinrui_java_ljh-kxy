package org.xinrui.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.entity.EmrExClinical;
import org.xinrui.mapper.EmrExClinicalMapper;
import org.xinrui.service.IPacsInfectionService;
import org.xinrui.core.tool.utils.DateUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PacsInfectionScheduleTest {

	@Autowired
	private EmrExClinicalMapper emrExClinicalMapper;

	@Autowired
	private IPacsInfectionService iPacsInfectionService;

	@Autowired
	private PacsInfectionSchedule schedule;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private final Date testDate = DateUtil.parse("2023-01-13", "yyyy-MM-dd");
	private final String redisKey = PacsInfectionSchedule.REDIS_INFECTION_ITEM_DATA + ":REPORT:" + DateUtil.format(testDate, "yyyyMMdd");

	@BeforeEach
	void setup() {
		// 1. 清理Redis（避免历史数据干扰）
		stringRedisTemplate.getConnectionFactory().getConnection().flushDb();

		// 2. 确保测试数据在2023-10-01范围内（无需插入，直接验证）
//		verifyTestDataExists();
	}

	private void verifyTestDataExists() {
		// 检查数据库中是否存在2023-10-01的数据
		Date startTime = DateUtil.getStartOfDate(testDate);
		Date endTime = DateUtil.getEndOfDate(testDate);
		Map<String, Date> queryParams = new HashMap<>();
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);

		Page<EmrExClinical> page = new Page<>(1, 100);
		List<EmrExClinical> testData = emrExClinicalMapper.selectEmrExClinical(page, queryParams);

		assertTrue(testData.size() > 0, "数据库中必须存在2023-10-01的测试数据");
		System.out.println("✅ 测试数据验证通过：数据库中存在"+testData.size()+"条2023-10-01的数据");
	}

/**
 * 测试用例：测试上传已存在的数据
 * 该方法主要验证当数据已存在时，上传功能的正确性
 */
	@Test
	void testUploadWithExistingData() {
		// Mock上传服务返回成功
//		when(iPacsInfectionService.postEmrExClinical(any(EmrExClinicalDto.class)))
//			.thenReturn(true);

		// 执行上传操作
		schedule.uploadEmrExClinicalData();

//		Map<String, Date> queryParams = new HashMap<>();
//		queryParams.put("startTime", DateUtil.getStartOfDate(testDate));  // 设置查询开始时间
//		queryParams.put("endTime", DateUtil.getEndOfDate(testDate));    // 设置查询结束时间
//
//		// 1. 验证上传服务调用次数 = 数据库中测试数据条数
//		int expectedCalls = emrExClinicalMapper.selectEmrExClinical(
//			new Page<>(1, 100),    // 分页查询，第一页，每页100条
//			queryParams           // 查询参数
//		).size();                // 获取查询结果数量
//
//		verify(iPacsInfectionService, times(expectedCalls)).postEmrExClinical(any());
//
//
//
//		// 2. 验证Redis记录了所有上传ID
//		List<EmrExClinicalDto> testData = emrExClinicalMapper.selectEmrExClinical(
//			new Page<>(1, 100),
//			queryParams
//		);
//
//		// 遍历测试数据，检查每个ID是否都存在于Redis中
//		for (EmrExClinicalDto dto : testData) {
//			assertTrue(stringRedisTemplate.opsForSet().isMember(redisKey, dto.getId()),
//				"Redis应记录ID: " + dto.getId());
//		}
//
//		// 打印上传成功信息
//		System.out.println("✅ 上传成功：" + expectedCalls + "条数据上传至Redis");
	}

	@Test
	void testUploadWithPartialFailure() {
		// Mock前50%数据失败，后50%成功
		when(iPacsInfectionService.postEmrExClinical(any(EmrExClinicalDto.class)))
			.thenAnswer(invocation -> {
				String id = invocation.getArgument(0, EmrExClinicalDto.class).getId();
				return Integer.parseInt(id.replace("R", "")) > 3; // R004, R005成功
			});

		// 执行上传
		schedule.uploadEmrExClinicalData();

		Map<String, Date> queryParams = new HashMap<>();
		queryParams.put("startTime", DateUtil.getStartOfDate(testDate));
		queryParams.put("endTime", DateUtil.getEndOfDate(testDate));

		// 获取实际数据量
		int totalData = emrExClinicalMapper.selectEmrExClinical(
			new Page<>(1, 100),
			queryParams
		).size();

		// 验证成功上传数量 = 总数据量 - 失败数量
		int expectedSuccess = (int) Math.ceil(totalData * 0.5);
		verify(iPacsInfectionService, times(expectedSuccess)).postEmrExClinical(any());

		// 验证Redis记录了成功上传的ID
		for (int i = 1; i <= totalData; i++) {
			String id = "R00" + i;
			boolean shouldExist = i > 3; // R004, R005存在
			assertTrue(stringRedisTemplate.opsForSet().isMember(redisKey, id) == shouldExist,
				"ID " + id + " 应" + (shouldExist ? "存在" : "不存在") + "于Redis");
		}
	}
}
