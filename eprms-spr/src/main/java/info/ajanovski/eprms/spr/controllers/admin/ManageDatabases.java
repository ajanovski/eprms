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

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.spr.services.GenericService;
import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.util.UserInfo;

@Controller
@SessionAttributes({ "userInfo", "isOnlyShowNotCreated" })
@Secured("ROLE_ADMINISTRATOR")
public class ManageDatabases {

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
		return Boolean.FALSE;
	}

	@GetMapping(path = { "admin/ManageDatabases" })
	public String ManageDatabases(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated) {
		model = modelOps.addMainModelAttribs("EPM - Admin - ManageDatabases", model, userInfo);
		model.addAttribute("databases", getDatabases(isOnlyShowNotCreated));
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		return "admin/ManageDatabases";
	}

	@GetMapping(path = { "admin/ManageDatabases.toggle" })
	public String ManageDatabasesToggle(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Teams", model, userInfo);
		isOnlyShowNotCreated = !isOnlyShowNotCreated;
		model.addAttribute("databases", getDatabases(isOnlyShowNotCreated));
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		return "admin/ManageDatabases";
	}

	@GetMapping(path = { "admin/ManageDatabases.delete" })
	@Transactional
	public String deleteDatabase(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated,
			@RequestParam(name = "databaseId") long databaseId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Teams", model, userInfo);
		model.addAttribute("databases", getDatabases(isOnlyShowNotCreated));
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);

		genericService.deleteByPK(Database.class, databaseId);

		return "redirect:admin/ManageDatabases";
	}

	@GetMapping(path = { "admin/ManageDatabases.edit" })
	public String editDatabase(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated,
			@RequestParam(name = "databaseId") long databaseId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Teams", model, userInfo);
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		model.addAttribute("databases", getDatabases(isOnlyShowNotCreated));

		Database editDatabase = genericService.getByPK(Database.class, databaseId);
		model.addAttribute("editDatabase", editDatabase);

		return "admin/ManageDatabases";
	}

	@PostMapping(path = "admin/ManageDatabases.saveDatabaseForm")
	@Transactional
	public String saveDatabaseForm(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("isOnlyShowNotCreated") Boolean isOnlyShowNotCreated,
			@ModelAttribute("editDatabase") Database editDatabase) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Teams", model, userInfo);
		model.addAttribute("isOnlyShowNotCreated", isOnlyShowNotCreated);
		model.addAttribute("databases", getDatabases(isOnlyShowNotCreated));

		Database databaseToSave = genericService.getByPK(Database.class, editDatabase.getDatabaseId());
		databaseToSave.setName(editDatabase.getName());
		databaseToSave.setOwner(editDatabase.getOwner());
		genericService.save(databaseToSave);
		return "redirect:admin/ManageDatabases";
	}

	@Inject
	private GenericService genericService;

	public List<Database> getDatabases(Boolean isOnlyShowNotCreated) {
		List<Database> ls = (List<Database>) genericService.getAll(Database.class);
		if (isOnlyShowNotCreated) {
			return ls.stream().filter(p -> p.getDateCreated() == null).collect(Collectors.toList());
		} else {
			return ls;
		}
	}

}
