package org.xinrui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.guangzhou.link.service.PacsGuangZhouLinkServiceImpl;
import org.guangzhou.link.vo.PacsPatientExtVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xinrui.core.tool.api.R;
import org.xinrui.core.tool.utils.DateUtil;
import org.xinrui.core.tool.utils.Func;
import org.xinrui.pacs.entity.PacsExamReport;
import org.xinrui.schedule.PacsToHisSchedule;

import java.util.Date;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/test/pacsToHis")
@RequiredArgsConstructor
@Api(tags = "PACS回传报告测试接口")
public class PacsToHisTestController {

	private final PacsToHisSchedule pacsToHisSchedule;

	private final PacsGuangZhouLinkServiceImpl pacsGuangZhouLinkService;

	/**
	 * 测试单个报告数据回传
	 */
	@GetMapping("/sendSingleReport")
	public void testSendReport(@RequestParam Long registrationOid) {
		try {
			log.info("【testSendReport】开始测试单个报告回传，registrationOid= {}", registrationOid);
			pacsToHisSchedule.sendReeportToHis(registrationOid);
		} catch (Exception e) {
			log.error("【testSendReport】测试单个报告回传失败：registrationOid={}, errMsg={}", registrationOid, e);
		}
	}

	/**
	 * 测试查询报告数据
	 */
	@GetMapping("/queryPacsDataByRegOid")
	public R<PacsPatientExtVO> queryPacsDataByRegOid(@RequestParam Long regOid) {
		log.info("【queryPacsData】测试查询报告数据，registrationOid: {}", regOid);
		PacsPatientExtVO pacsData = pacsGuangZhouLinkService.getPacsData(regOid);
		if(Func.isEmpty(pacsData)){
			log.info("【queryPacsData】没有查询到报告数据：registrationOid={}", regOid);
			return R.success("没有查询到报告数据");
		}
		log.info("【queryPacsData】测试查询报告数据成功：registrationOid={}, pacsData={}", regOid, pacsData);
		return R.data(pacsData);
	}

	/**
	 * 测试查询报告数据
	 */
	@GetMapping("/queryPacsData")
	public R<Page<PacsExamReport>> queryPacsData(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
		Date now = new Date();
		Page<PacsExamReport> pacsExamReportPage = pacsGuangZhouLinkService.pageReport(pageNum, pageSize, DateUtil.minusDays(now, 2), now);
		return R.data(pacsExamReportPage);
	}
}
