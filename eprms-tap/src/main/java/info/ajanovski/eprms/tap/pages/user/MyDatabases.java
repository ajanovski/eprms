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

package info.ajanovski.eprms.tap.pages.user;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.ResourceManager;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
public class MyDatabases {
	@Property
	@SessionState
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Inject
	private ResourceManager resourceManager;

	@Inject
	private SystemConfigService systemConfigService;

	@Property
	private Database database;

	public List<Database> getProjectDatabases() {
		return resourceManager.getActiveDatabasesByProject(userInfo.getPersonId());
	}

	public String getURLDBSoftware() {
		return systemConfigService.getString("URL-DB-Software");
	}

}
