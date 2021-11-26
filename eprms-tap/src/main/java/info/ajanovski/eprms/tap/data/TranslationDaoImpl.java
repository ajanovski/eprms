package info.ajanovski.eprms.tap.data;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import info.ajanovski.eprms.model.entities.Translation;

public class TranslationDaoImpl implements TranslationDao {

	@Inject
	private Session session;

	@Inject
	private GenericDao genericDao;

	private Session getEntityManager() {
		return session.getSession();
	}

	@Override
	public String getTranslation(String className, String attributeCode, long originalObjectId, String locale) {
		try {
			Translation t = (Translation) getEntityManager().createQuery("""
					from Translation
					where className=:className and
					      attributeCode=:attributeCode and
					      originalObjectId=:originalObjectId and
					      locale=:locale
					""").setParameter("className", className).setParameter("attributeCode", attributeCode)
					.setParameter("originalObjectId", originalObjectId).setParameter("locale", locale)
					.getSingleResult();
			return t.getTranslatedText();
		} catch (Exception e) {
			return null;
		}
	}

}
