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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import info.ajanovski.eprms.spr.util.AppConfig;

@Controller
public class LoginLogout {
	private final Logger logger = LoggerFactory.getLogger(LoginLogout.class);

	@GetMapping("Login")
	public String login() {
		logger.info("Login called");
		return "redirect:/MyProfile";
	}

	@GetMapping(path = { "Logout" })
	public String logout(HttpServletRequest request, HttpServletResponse response,
			SecurityContextLogoutHandler logoutHandler, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logoutHandler.logout(request, response, auth);
		new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
				.logout(request, response, auth);
		model.addAttribute("casLogoutLink",
				AppConfig.getString("cas.server") + "/cas/logout?service=" + AppConfig.getString("app.server"));
		return "Logout";
	}

}
