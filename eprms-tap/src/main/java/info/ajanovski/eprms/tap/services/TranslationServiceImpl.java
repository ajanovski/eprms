package info.ajanovski.eprms.tap.services;

import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.tap.data.TranslationDao;

public class TranslationServiceImpl implements TranslationService {

	@Inject
	private TranslationDao translationDao;

	@Override
	public String getTranslation(String className, String attributeCode, long originalObjectId, String locale) {
		return translationDao.getTranslation(className, attributeCode, originalObjectId, locale);
	}

}
