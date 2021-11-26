package info.ajanovski.eprms.tap.services;

import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.SystemParameter;

public class SystemConfigServiceImpl implements SystemConfigService {

	@Inject
	private GenericService genericService;

	@Override
	public String getString(String key) {
		SystemParameter sp = genericService.getByCode(SystemParameter.class, key);
		if (sp != null) {
			return sp.getValue();
		} else {
			return null;
		}
	}
}
