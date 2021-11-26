package info.ajanovski.eprms.tap.data;

public interface TranslationDao {

	public String getTranslation(String className, String AttributeCode, long originalObjectId, String locale);

}
