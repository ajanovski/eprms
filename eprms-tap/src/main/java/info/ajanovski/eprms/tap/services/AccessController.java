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

package info.ajanovski.eprms.tap.services;

import java.io.IOException;

import org.apache.tapestry5.http.services.Response;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.slf4j.Logger;

import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.PublicPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.pages.Index;
import info.ajanovski.eprms.tap.util.AppConstants;
import info.ajanovski.eprms.tap.util.UserInfo;

public class AccessController implements ComponentRequestFilter {

	private ApplicationStateManager applicationStateManager;
	private final ComponentSource componentSource;

	@Inject
	private Logger logger;

	@Inject
	private Response response;

	@Inject
	private PageRenderLinkSource linkSource;

	public AccessController(ApplicationStateManager asm, ComponentSource componentSource) {
		this.applicationStateManager = asm;
		this.componentSource = componentSource;
	}

	public boolean checkAccess(String pageName) throws IOException {
		boolean hasAccessAnnotation = false;
		logger.debug("check access for {}", pageName);
		if (pageName.equals("") || pageName.equals("/")) {
			pageName = AppConstants.PageIndex;
		}

		Component page = null;
		page = componentSource.getPage(pageName);

		boolean publicPage = page.getClass().getAnnotation(PublicPage.class) != null;
		boolean studentPage = page.getClass().getAnnotation(StudentPage.class) != null;
		boolean instructorPage = page.getClass().getAnnotation(InstructorPage.class) != null;
		boolean adminPage = page.getClass().getAnnotation(AdministratorPage.class) != null;

		hasAccessAnnotation = publicPage | studentPage | instructorPage | adminPage;
		UserInfo userInfo = applicationStateManager.getIfExists(UserInfo.class);

		boolean canAccess = false;
		if (publicPage) {
			logger.debug("Accessing a public page.");
			canAccess = true;
		} else {
			if (userInfo == null) {
				logger.debug("UserInfo is null");
			} else {
				logger.debug("userInfo is not null");
				if (studentPage) {
					logger.debug("studentPage");
					canAccess = canAccess || userInfo.isStudent();
				}
				if (adminPage) {
					logger.debug("adminPage");
					canAccess = canAccess || userInfo.isAdministrator();
				}
			}
		}

		if (canAccess && hasAccessAnnotation) {
			logger.debug("checkAccess: ACCESS GRANTED to page:{} canaccess:{} hasaccessannotation:{} ", pageName,
					canAccess, hasAccessAnnotation);
			return true;
		} else {
			logger.debug("checkAccess: ACCESS DENIED to page:{} canaccess:{} hasaccessannotation:{} ", pageName,
					canAccess, hasAccessAnnotation);
			return false;
		}
	}

	@Override
	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {
		boolean accessOK = checkAccess(parameters.getContainingPageName());
		if (accessOK) {
			handler.handleComponentEvent(parameters);
		} else {
			logger.error("handleComponentEvent: ACCESS DENIED TO {} {} {}", parameters.getEventType(),
					parameters.getNestedComponentId(), parameters.getContainingPageName());

			response.sendRedirect(linkSource.createPageRenderLink(Index.class));
		}
	}

	@Override
	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {
		boolean accessOK = checkAccess(parameters.getLogicalPageName());
		if (accessOK) {
			handler.handlePageRender(parameters);
		} else {
			logger.error("handlePageRender: ACCESS DENIED TO {}", parameters.getLogicalPageName());

			response.sendRedirect(linkSource.createPageRenderLink(Index.class));
		}
	}
}
