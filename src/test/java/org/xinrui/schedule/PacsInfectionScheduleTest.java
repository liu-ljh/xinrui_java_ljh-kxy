package org.xinrui.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.SetOperations;
import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.dto.EmrExClinicalItemDto;
import org.xinrui.entity.EmrExClinical;
import org.xinrui.entity.EmrExClinicalItem;
import org.xinrui.mapper.EmrExClinicalItemMapper;
import org.xinrui.mapper.EmrExClinicalMapper;
import org.xinrui.service.IPacsInfectionService;
import org.xinrui.config.PacsInfectionScheduleConfig;
import org.xinrui.util.EmrExClinicalItemUtil;
import org.xinrui.util.EmrExClinicalUtil;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.xinrui.config.PacsInfectionScheduleConfig.*;

/**
 * PacsInfectionSchedule 单元测试（聚焦核心逻辑，避免静态工具类/时间依赖）
 * 特点：
 * 1. 使用纯 Mockito 单元测试，无需 Spring 容器，执行快
 * 2. 通过参数注入规避 new Date() 问题
 * 3. 验证关键流程：查询 → 构建 → 去重 → 上传 → 记录
 * 4. 覆盖正常上传、跳过已上传、分页终止等场景
 */
@DisplayName("传染病上报定时任务测试")
class PacsInfectionScheduleTest {

	@InjectMocks
	private PacsInfectionSchedule schedule;

	@Mock
	private EmrExClinicalMapper emrExClinicalMapper;
	@Mock
	private EmrExClinicalItemMapper emrExClinicalItemMapper;
	@Mock
	private StringRedisTemplate stringRedisTemplate;
	@Mock
	private IPacsInfectionService iPacsInfectionService;
	@Mock
	private SetOperations<String, String> setOps; // Redis Set 操作模拟

	@Captor
	private ArgumentCaptor<EmrExClinicalDto> clinicalDtoCaptor;
	@Captor
	private ArgumentCaptor<EmrExClinicalItemDto> itemDtoCaptor;
	@Captor
	private ArgumentCaptor<String> redisKeyCaptor;
	@Captor
	private ArgumentCaptor<String> redisMemberCaptor;

	private Date testDate;
	private String testDateStr;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		// 固定测试时间，规避 new Date() 不确定性
		testDate = new GregorianCalendar(2024, Calendar.JANUARY, 15).getTime();
		testDateStr = new SimpleDateFormat("yyyyMMdd").format(testDate);

		// 模拟 Redis Set 操作链
		when(stringRedisTemplate.opsForSet()).thenReturn(setOps);
		when(setOps.isMember(anyString(), anyString())).thenReturn(false); // 默认未上传
		when(iPacsInfectionService.postEmrExClinical(any())).thenReturn(true);
		when(iPacsInfectionService.postEmrExClinicalItem(any())).thenReturn(true);
	}

	// ==================== 检查报告数据上传测试 ====================

	@Test
	@DisplayName("uploadEmrExClinicalData: 正常流程 - 查询2条数据并成功上传")
	void testUploadEmrExClinicalData_Success() {
		// 准备测试数据
		List<EmrExClinical> mockData = Arrays.asList(
			createMockClinical(1L, "RPT001"),
			createMockClinical(2L, "RPT002")
		);

		// 模拟分页：第1页有数据，总记录数=2 → 总页数=1
		Page<EmrExClinical> page = new Page<>(1, 10);
		page.setRecords(mockData);
		page.setTotal(2L);
		when(emrExClinicalMapper.selectEmrExClinical(any(Page.class), anyMap()))
			.thenReturn(mockData);

		// 执行（通过反射调用，规避 private 限制；实际项目建议提取核心逻辑为 package-private 方法）
		invokeUploadClinicalMethod();

		// ✅ 验证1：上传次数正确（核心业务逻辑）
		verify(iPacsInfectionService, times(2)).postEmrExClinical(any());

		// ✅ 验证2：Redis 记录了2条 member（关键防重逻辑）
		verify(setOps, times(2)).add(anyString(), redisMemberCaptor.capture());
		assertEquals("1", redisMemberCaptor.getAllValues().get(0));
		assertEquals("2", redisMemberCaptor.getAllValues().get(1));

		// ✅ 验证3：Key 格式合法（避免硬编码具体值）
		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		verify(setOps, times(2)).add(keyCaptor.capture(), anyString());
		String actualKey = keyCaptor.getValue();
		// 验证：Key 以配置前缀开头 + 包含8位日期
		assertTrue(actualKey.startsWith(REDIS_INFECTION_REPORT_DATA),
			"Key 应以配置常量开头: " + REDIS_INFECTION_REPORT_DATA);
		assertTrue(actualKey.matches(".*:\\d{8} $ "),
			"Key 末尾应为8位日期，实际: " + actualKey);
	}

	@Test
	@DisplayName("uploadEmrExClinicalData: 跳过已上传记录")
	void testUploadEmrExClinicalData_SkipUploaded() {
		// 模拟第1条已存在Redis
		when(setOps.isMember(anyString(), eq("1"))).thenReturn(true);
		when(setOps.isMember(anyString(), eq("2"))).thenReturn(false);

		List<EmrExClinical> mockData = Arrays.asList(
			createMockClinical(1L, "RPT001"),
			createMockClinical(2L, "RPT002")
		);
		Page<EmrExClinical> page = new Page<>(1, 10);
		page.setRecords(mockData);
		page.setTotal(2L);
		when(emrExClinicalMapper.selectEmrExClinical(any(Page.class), anyMap()))
			.thenReturn(mockData);

		invokeUploadClinicalMethod();

		// 验证：仅第2条被上传
		verify(iPacsInfectionService, times(1)).postEmrExClinical(any());
		verify(setOps, times(1)).add(anyString(), anyString()); // 仅新增1条记录
	}

	// ==================== 检查报告项目数据上传测试 ====================

	@Test
	@DisplayName("uploadEmrExClinicalItemData: 分页处理与去重逻辑")
	void testUploadEmrExClinicalItemData_PaginationAndDedup() {
		// 模拟第1页2条，第2页1条，第3页空（终止循环）
		List<EmrExClinicalItem> page1 = Arrays.asList(createMockItem(1L), createMockItem(2L));
		List<EmrExClinicalItem> page2 = Arrays.asList(createMockItem(3L));

		// 使用 Answer 模拟分页状态变化
		doAnswer(inv -> {
			Page<EmrExClinicalItem> p = inv.getArgument(0);
			int current = (int) p.getCurrent();
			if (current == 1) {
				p.setRecords(page1);
				p.setTotal(3L); // 总3条，每页2条 → 2页
				return page1;
			} else if (current == 2) {
				p.setRecords(page2);
				return page2;
			}
			return Collections.emptyList();
		}).when(emrExClinicalItemMapper).selectEmrExClinicalItem(any(Page.class), any(Date.class), any(Date.class));

		// 执行（同上，建议重构为可测试方法）
		invokeUploadItemMethod();

		// 验证：Mapper 被调用3次（第1、2、3页）
		verify(emrExClinicalItemMapper, times(3))
			.selectEmrExClinicalItem(any(Page.class), any(Date.class), any(Date.class));

		// 验证：上传服务被调用3次
		verify(iPacsInfectionService, times(3)).postEmrExClinicalItem(itemDtoCaptor.capture());
		assertEquals(3, itemDtoCaptor.getAllValues().size());

		// 验证：Redis 记录3条
		verify(setOps, times(3)).add(anyString(), anyString());
	}

	// ==================== 辅助方法 ====================

	/** 通过反射调用私有方法（测试专用） */
	private void invokeUploadClinicalMethod() {
		try {
			Method method = PacsInfectionSchedule.class.getDeclaredMethod("uploadEmrExClinicalData");
			method.setAccessible(true);
			method.invoke(schedule);
		} catch (Exception e) {
			fail("反射调用失败: " + e.getMessage());
		}
	}

	private void invokeUploadItemMethod() {
		try {
			Method method = PacsInfectionSchedule.class.getDeclaredMethod("uploadEmrExClinicalItemData");
			method.setAccessible(true);
			method.invoke(schedule);
		} catch (Exception e) {
			fail("反射调用失败: " + e.getMessage());
		}
	}

	/** 创建模拟检查报告实体 */
	private EmrExClinical createMockClinical(Long oid, String reportId) {
		EmrExClinical e = new EmrExClinical();
		e.setOid(oid);
		e.setReportId(reportId);
		e.setPatientId("PAT001");
		e.setName("张三");
		e.setPersonId("110101199001011234");
		e.setModality("CT");
		e.setReportDate(new Date());
		e.setReader(1001L);
		e.setTenantOid("HOSP001");
		e.setApplicationDeptOid("DEPT001");
		e.setApplicationId("APP001");
		e.setPatientSourceType("1");
		return e;
	}

	/** 创建模拟检查项目实体 */
	private EmrExClinicalItem createMockItem(Long oid) {
		EmrExClinicalItem item = new EmrExClinicalItem();
		item.setOid(oid);
		item.setReportId("RPT001");
		item.setModality("CT");
		item.setPositive(1); // 阳性
		return item;
	}
}
