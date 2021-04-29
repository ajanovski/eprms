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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.util.AppConfig;

@Controller
public class Logout {
	private final Logger logger = LoggerFactory.getLogger(Logout.class);

	@Inject
	private ModelConstructor modelOps;

	@Inject
	public HttpServletRequest request;

	@Inject
	public PersonManager personManager;

	@GetMapping(path = { "Logout" })
	public String onActivate(Model model, HttpServletRequest request, HttpSession session) {
		model = modelOps.addPublicModelAttribs("ERPMS - Logout", model);
		model.addAttribute("casServer", request.getServletContext().getInitParameter("casServerLogoutUrl"));
		model.addAttribute("appServer", request.getServletContext().getInitParameter("service"));
		model.addAttribute("logoutRedirectToServer", AppConfig.getString("logout.redirectToServer"));

		// Clear session
		// Session session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			logger.debug("Session successfully invalidated!");
		}

		clearCookie();
		return "Logout";
	}

	private void clearCookie() {
	}

}
