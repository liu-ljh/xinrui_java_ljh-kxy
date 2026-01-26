package org.xinrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xinrui.dto.EmrExClinicalDto;

import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xinrui.entity.EmrExClinical;


@Mapper
public interface EmrExClinicalMapper extends BaseMapper<EmrExClinicalDto> {
	List<EmrExClinical> selectEmrExClinical(Page<EmrExClinical> page, Map<String, Date> query);
}
