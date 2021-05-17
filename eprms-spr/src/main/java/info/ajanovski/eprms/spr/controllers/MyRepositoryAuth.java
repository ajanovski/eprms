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

import java.security.MessageDigest;
import java.util.Base64;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.spr.services.GenericService;
import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.util.UserInfo;

@Controller
@SessionAttributes("userInfo")
public class MyRepositoryAuth {
	private static final Logger logger = LoggerFactory.getLogger(MyRepositoryAuth.class);

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

	@Inject
	private GenericService genericService;

	@GetMapping(path = { "MyRepositoryAuth" })
	public String MyDatabases(Model model, @ModelAttribute("userInfo") UserInfo userInfo) {
		model = modelOps.addMainModelAttribs("EPM - MyRepositoryAuth", model, userInfo);
		return "MyRepositoryAuth";
	}

	@PostMapping(path = "MyRepositoryAuth")
	@Transactional
	public String MyRepositoryAuth(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@RequestParam(defaultValue = "", required = true, name = "password") String password,
			@RequestParam(defaultValue = "", required = true, name = "confirmPassword") String confirmPassword) {
		model = modelOps.addMainModelAttribs("EPM - MyRepositoryAuth", model, userInfo);

		if (password != null && confirmPassword != null && password.equals(confirmPassword)) {
			Person myself = genericService.getByPK(Person.class, userInfo.getPersonId());
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.reset();
				md.update(password.getBytes("UTF-8"));
				String newp = "{SHA}" + Base64.getEncoder().encodeToString(md.digest());
				myself.setAuthString(newp);
				genericService.save(myself);
			} catch (Exception e) {
				logger.error("Exception {}", e);
			}
		} else {
			logger.debug("empty pass supplied by {}", userInfo.getUserName());
		}

		return "MyRepositoryAuth";
	}

	public String getClassForPageName() {
		return "active";
	}

	public String getPageNameTitle(String pageName) {
		return pageName + "-pagelink";
	}

}
