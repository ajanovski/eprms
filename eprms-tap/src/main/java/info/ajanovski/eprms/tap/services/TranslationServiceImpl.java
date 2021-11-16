package info.ajanovski.eprms.tap.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import info.ajanovski.eprms.model.entities.Translation;

public class TranslationServiceImpl implements TranslationService {

	@Inject
	private Session session;

	@Inject
	private GenericService genericService;

	@Override
	public String getTranslation(String className, String attributeCode, long originalObjectId, String locale) {
		try {
			Criteria crit = session.createCriteria(Translation.class)
					.add(Restrictions.and(Restrictions.eq("className", className),
							Restrictions.eq("attributeCode", attributeCode), Restrictions.eq("locale", locale),
							Restrictions.eq("originalObjectId", originalObjectId)))
					.setReadOnly(true);

			return ((Translation) crit.uniqueResult()).getTranslatedText();
		} catch (Exception e) {
			return null;
		}
	}

}
