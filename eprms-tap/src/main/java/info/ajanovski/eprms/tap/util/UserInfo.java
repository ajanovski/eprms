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

package info.ajanovski.eprms.tap.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.http.services.RequestGlobals;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.services.PersonManager;

public class UserInfo {
	enum UserRole {
		NONE, STUDENT, INSTRUCTOR, ADMINISTRATOR
	};

	private String userName;
	private Long personId;
	private List<UserRole> userRoles;

	private PersonManager pm;
	private Logger logger;

	public UserInfo(@Inject RequestGlobals requestGlobals, @Inject Logger logger, @Inject PersonManager pm)
			throws Exception {
		this.pm = pm;
		this.logger = logger;

		if (requestGlobals != null) {
			if (requestGlobals.getHTTPServletRequest().getRemoteUser() != null) {
				this.userName = requestGlobals.getHTTPServletRequest().getRemoteUser();
				this.setupUser();
			} else {
				this.userName = null;
			}
		} else {
			this.userName = null;
		}
	}

	private void setupUser() throws Exception {
		if (userName != null) {
			logger.info("Logged in user: {}", userName);
			userRoles = new ArrayList<UserRole>();
			Person p = pm.getPersonByUsername(userName);
			if (p == null) {
				personId = null;
				userRoles.clear();
			} else {
				this.personId = Long.valueOf(p.getPersonId());
				if (personId != null) {
					for (PersonRole pr : pm.getPersonRolesForPerson(personId)) {
						if (pr.getRole().getName().equals(ModelConstants.RoleAdministrator)) {
							userRoles.add(UserRole.ADMINISTRATOR);
						} else if (pr.getRole().getName().equals(ModelConstants.RoleInstructor)) {
							userRoles.add(UserRole.INSTRUCTOR);
						} else if (pr.getRole().getName().equals(ModelConstants.RoleStudent)) {
							userRoles.add(UserRole.STUDENT);
						}
					}
					if (userRoles.size() == 0) {
						userRoles.add(UserRole.NONE);
					}
				}
			}
		}

	}

	public String getUserName() {
		return userName;
	}

	public Long getPersonId() {
		return personId;
	}

	public boolean isNone() {
		return userRoles.contains(UserRole.NONE);
	}

	public boolean isStudent() {
		return userRoles.contains(UserRole.STUDENT);
	}

	public boolean isAdministrator() {
		return userRoles.contains(UserRole.ADMINISTRATOR);
	}

	public void impersonate(String inUsername) throws Exception {
		this.userName = inUsername;
		this.setupUser();
	}

}
