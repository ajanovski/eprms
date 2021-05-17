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

package info.ajanovski.eprms.spr.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.spr.services.PersonManager;

public class UserInfo {
	enum UserRole {
		NONE, STUDENT, INSTRUCTOR, ADMINISTRATOR
	};

	private String userName;
	private Long personId;
	private List<UserRole> userRoles;

	private PersonManager pm;
	private static final Logger logger = LoggerFactory.getLogger(UserInfo.class);

	public UserInfo(PersonManager pm) {
		if (pm != null) {
			this.pm = pm;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() != null) {
				logger.info("Login by: {}", auth.getPrincipal());
				userName = ((UserDetails) auth.getPrincipal()).getUsername();
				this.setupUser();
			} else {
				userName = null;
			}
		} else {
			userName = null;
		}
	}

	private void setupUser() {
		if (userName != null) {
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
