package info.ajanovski.eprms.tap.services;

public interface TranslationService {

	public String getTranslation(String className, String AttributeCode, long originalObjectId, String locale);

}
