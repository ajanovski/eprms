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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.util.UserInfo;

@Controller
@SessionAttributes("userInfo")
public class MyProfile {
	private final Logger logger = LoggerFactory.getLogger(LoginLogout.class);

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

	@GetMapping(path = { "MyProfile" })
	public String Index(Model model, @ModelAttribute("userInfo") UserInfo userInfo) {
		model = modelOps.addMainModelAttribs("EPM - MyDatabases", model, userInfo);
		return "MyProfile";
	}

}
