/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource 
 * Management System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *     
 * EPRMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 ******************************************************************************/

package info.ajanovski.eprms.tap.components;

import java.util.Locale;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.PersistentLocale;
import org.slf4j.Logger;

import info.ajanovski.eprms.tap.annotations.PublicPage;

@Import(stylesheet = { "site-overrides.css" }, module = { "bootstrap/dropdown", "bootstrap/collapse" })
@PublicPage
public class PublicLayout {

	@Inject
	private ComponentResources resources;

	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	private String pageName;

	@Property
	@Inject
	@Symbol(SymbolConstants.APPLICATION_VERSION)
	private String appVersion;

	@Inject
	private Logger logger;

	public String[] getPublicPageNames() {
		return new String[] {};
	}

	public String getClassForPageName() {
		logger.debug("respgname:{}", resources.getPageName() + " " + resources.getCompleteId());
		if (resources.getPageName().equalsIgnoreCase(pageName)) {
			return "active";
		} else {
			return " ";
		}
	}

	@Inject
	private Messages messages;

	public String getPageNameTitle() {
		return messages.get(pageName + "-pagelink");
	}

	@Inject
	private PersistentLocale persistentLocale;

	void onActionFromLocaleToggle() {
		if (persistentLocale.isSet()) {
			if ("mk".equalsIgnoreCase(persistentLocale.get().getLanguage())) {
				persistentLocale.set(new Locale("en"));
			} else {
				persistentLocale.set(new Locale("mk"));
			}
		} else {
			persistentLocale.set(new Locale("mk"));
		}
	}

	public String getDisplayLanguage() {
		Locale loc = persistentLocale.get();
		if (loc == null)
			return "";
		else
			return loc.getLanguage().toUpperCase();
	}

}
