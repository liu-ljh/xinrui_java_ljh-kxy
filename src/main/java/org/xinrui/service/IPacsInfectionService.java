package org.xinrui.service;

import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.dto.EmrExClinicalItemDto;

public interface IPacsInfectionService {
	public boolean postEmrExClinical(EmrExClinicalDto emrExClinicalDatum);

	public boolean postEmrExClinicalItem(EmrExClinicalItemDto emrExClinicalItemDatum);

	public void  clearRedis();
}
