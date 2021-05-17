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

package info.ajanovski.eprms.spr.services;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import info.ajanovski.eprms.spr.util.UserInfo;

@Component
public class ModelConstructor {

	@Inject
	public String[] studentPageNames;

	@Inject
	public String[] adminPageNames;

	public Model addMainModelAttribs(String title, Model model, @ModelAttribute("userInfo") UserInfo userInfo) {
		model.addAttribute("title", title);
		model.addAttribute("studentPageNames", studentPageNames);
		model.addAttribute("adminPageNames", adminPageNames);
		model.addAttribute("classForPageName", "nav-link");
		model.addAttribute("displayLanguage", "en");
		if (userInfo != null) {
			model.addAttribute("userName", userInfo.getUserName());
			model.addAttribute("userInfo", userInfo);
		}
		return model;
	}

	public Model addMainModelNoUserAttribs(String title, Model model) {
		model.addAttribute("title", title);
		model.addAttribute("studentPageNames", studentPageNames);
		model.addAttribute("adminPageNames", adminPageNames);
		model.addAttribute("classForPageName", "nav-link");
		model.addAttribute("displayLanguage", "en");
		return model;
	}

	public Model addPublicModelAttribs(String title, Model model) {
		model.addAttribute("title", title);
		model.addAttribute("classForPageName", "nav-link");
		model.addAttribute("displayLanguage", "en");
		return model;
	}

}
