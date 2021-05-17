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

package info.ajanovski.eprms.spr.controllers.admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.spr.services.GenericService;
import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.util.UserInfo;

@Controller
@SessionAttributes({ "userInfo", "isOnlyShowNotCreated" })
@Secured("ROLE_ADMINISTRATOR")
public class ManageRepositories {
	private static final Logger logger = LoggerFactory.getLogger(ManageRepositories.class);

	@Inject
	private ModelConstructor modelOps;

	@Inject
	public HttpServletRequest request;

	@Inject
	public PersonManager personManager;

	@ModelAttribute("userInfo")
	public UserInfo userInfo() {
		return new UserInfo(personManager);
	}

	@ModelAttribute("isOnlyShowNotCreated")
	public Boolean isOnlyShowNotCreated() {
		logger.info("setup session attribute isOnlyShowNotCreated");
		return Boolean.FALSE;
	}

	@Inject
	private GenericService genericService;

	@GetMapping(path = { "admin/ManageRepositories" })
	public String ManageRepositories(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated) {
		model = modelOps.addMainModelAttribs("EPM - Admin - ManageRepositories", model, userInfo);
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		model.addAttribute("repositories", getRepositories(isOnlyShowNotCreated));
		return "admin/ManageRepositories";
	}

	@GetMapping(path = { "admin/ManageRepositories.toggle" })
	public String ManageRepositoriesToggle(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Teams", model, userInfo);
		isOnlyShowNotCreated = !isOnlyShowNotCreated;
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		model.addAttribute("repositories", getRepositories(isOnlyShowNotCreated));
		return "admin/ManageRepositories";
	}

	@GetMapping(path = { "admin/Managerepositories.delete" })
	@Transactional
	public String onDeleteRepository(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated,
			@RequestParam(name = "repositoryId") long repositoryId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Teams", model, userInfo);
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		model.addAttribute("repositories", getRepositories(isOnlyShowNotCreated));
		deleteRepository(repositoryId);
		return "redirect:admin/ManageRepositories";
	}

	public void deleteRepository(long repositoryId) {
		genericService.deleteByPK(Repository.class, repositoryId);
	}

	public List<Repository> getRepositories(Boolean isOnlyShowNotCreated) {
		List<Repository> ls = (List<Repository>) genericService.getAll(Repository.class);
		if (isOnlyShowNotCreated) {
			return ls.stream().filter(p -> p.getDateCreated() == null).collect(Collectors.toList());
		} else {
			return ls;
		}
	}
}
