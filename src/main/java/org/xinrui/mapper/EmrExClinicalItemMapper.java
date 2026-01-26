package org.xinrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xinrui.dto.EmrExClinicalItemDto;
import org.xinrui.entity.EmrExClinicalItem;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmrExClinicalItemMapper {

	List<EmrExClinicalItem> selectEmrExClinicalItem(@Param("page") Page<EmrExClinicalItem> page,
													@Param("startTime") Date startTime, @Param("endTime")Date endTime);
}
