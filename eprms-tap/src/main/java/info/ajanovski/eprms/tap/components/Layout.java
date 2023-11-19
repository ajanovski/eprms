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
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.PersistentLocale;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@Import(stylesheet = { "site-overrides.css" }, module = { "bootstrap/dropdown", "bootstrap/collapse" })
public class Layout {

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
	@Inject
	private GenericService genericService;

	@Inject
	private PersonManager personManager;

	public String getClassForPageName() {
		logger.debug("respgname:{}", resources.getPageName() + " " + resources.getCompleteId());
		if (resources.getPageName().equalsIgnoreCase(pageName)) {
			return "active";
		} else {
			return " ";
		}
	}

	@Property
	@SessionState
	private UserInfo userInfo;

	public String[] getStudentPageNames() {
		if (userInfo.isStudent() || userInfo.isAdministrator() || userInfo.isInstructor()) {
			return new String[] { "user/MyProjects", "user/MyProjectReports", "user/Discussions", "user/MyDatabases",
					"user/MyRepositories", "user/MyRepositoryAuth" };
		} else {
			return null;
		}
	}

	public String[] getProjectManagerPageNames() {
		if (userInfo.isAdministrator() || userInfo.isInstructor()) {
			return new String[] { "projectmanager/ProjectOverviewTickets", "projectmanager/ProjectOverviewTimeline",
					"admin/ManageCourses", "admin/ManageProjects", "admin/OverallCourseReport" };
		} else {
			return null;
		}
	}

	public String[] getAdminPageNames() {
		if (userInfo.isAdministrator()) {
			return new String[] { "admin/ManageActivityTypes", "admin/ManagePersons", "admin/ManageTeams",
					"admin/ProjectAutomation", "admin/Translations", "admin/SystemParameters", "admin/ManageDatabases",
					"admin/ManageRepositories" };
		} else {
			return null;
		}
	}

	public String getLoggedInUserName() {
		if (userInfo == null) {
			return "NOT LOGGED IN";
		} else {
			return personManager.getPersonFullNameWithId(genericService.getByPK(Person.class, userInfo.getPersonId()));
		}
	}

	@Inject
	private Messages messages;

	public String getPageNameTitle() {
		return messages.get(pageName + "-pagelink");
	}

	@Inject
	private PersistentLocale persistentLocale;

	void setupRender() {
		if (persistentLocale.get() == null) {
			persistentLocale.set(new Locale("mk"));
		}
	}

	public Object onActionFromLocaleToggle() {
		if ("en".equalsIgnoreCase(persistentLocale.get().getLanguage())) {
			persistentLocale.set(new Locale("mk"));
		} else {
			persistentLocale.set(new Locale("en"));
		}
		return this;
	}

	public String getDisplayLanguage() {
		return persistentLocale.get().toLanguageTag();
	}

}
