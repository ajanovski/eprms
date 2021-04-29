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

package info.ajanovski.eprms.spr.controllers;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.services.ResourceManager;
import info.ajanovski.eprms.spr.util.UserInfo;

@Controller
@SessionAttributes("userInfo")
public class MyRepositories {
	private final Logger logger = LoggerFactory.getLogger(MyDatabases.class);

	@Inject
	private ModelConstructor modelOps;

	@Inject
	public HttpServletRequest request;

	@Inject
	public PersonManager personManager;

	@ModelAttribute("userInfo")
	public UserInfo userInfo() {
		return new UserInfo(request, personManager);
	}

	@GetMapping(path = { "MyRepositories" })
	public String MyRepositories(Model model, @ModelAttribute("userInfo") UserInfo userInfo) {
		model = modelOps.addMainModelAttribs("EPM - MyRepositories", model, userInfo);
		model.addAttribute("personalRepositories", getPersonalRepositories(userInfo.getPersonId()));
		model.addAttribute("teamRepositories", getTeamRepositories(userInfo.getPersonId()));
		model.addAttribute("projectRepositories", getProjectRepositories(userInfo.getPersonId()));
		return "MyRepositories";
	}

	public String getClassForPageName() {
		return "active";
	}

	public String getPageNameTitle(String pageName) {
		return pageName + "-pagelink";
	}

	@Inject
	private ResourceManager resourceManager;

	public List<Repository> getPersonalRepositories(long personId) {
		return resourceManager.getActiveRepositoriesByPerson(personId);
	}

	public List<Repository> getTeamRepositories(long personId) {
		return resourceManager.getActiveRepositoriesByTeam(personId);
	}

	public List<Repository> getProjectRepositories(long personId) {
		return resourceManager.getActiveRepositoriesByProject(personId);
	}

}
