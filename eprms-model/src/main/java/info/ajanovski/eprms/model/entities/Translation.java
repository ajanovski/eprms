package info.ajanovski.eprms.model.entities;

import java.util.*;
import javax.persistence.*;

/*
*/
@Entity
@Table (schema="epm_util", name="translations")
public class Translation implements java.io.Serializable {

	private long	translationId;
	private String className;
	private long	originalObjectId;
	private String	locale;
	private String	attributeCode;
	private String	translatedText;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "translation_id", unique = true, nullable = false)
	public long getTranslationId() {
		return this.translationId;
	}

	public void setTranslationId(long translationId) {
		this.translationId = translationId;
	}

	@Column(name = "class_name", nullable = false)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className=className;
	}

	@Column(name = "original_object_id", nullable = false)
	public long getOriginalObjectId() {
		return this.originalObjectId;
	}

	public void setOriginalObjectId(long originalObjectId) {
		this.originalObjectId = originalObjectId;
	}

	@Column(name = "locale", nullable = false)
	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Column(name = "attribute_code", nullable = false)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "translated_text", nullable = false)
	public String getTranslatedText() {
		return this.translatedText;
	}

	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}
}
