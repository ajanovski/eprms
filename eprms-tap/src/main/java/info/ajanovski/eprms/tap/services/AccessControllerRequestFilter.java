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

public class AccessControllerRequestFilter implements ComponentRequestFilter {

	private final ApplicationStateManager applicationStateManager;
	private final ComponentSource componentSource;
	private final Logger logger;

	private Response response;
	private PageRenderLinkSource linkSource;

	public AccessControllerRequestFilter(final ApplicationStateManager asm, final ComponentSource componentSource,
			final Logger logger, Response response, PageRenderLinkSource linkSource) {
		logger.info("AccessController ComponentRequestFilter constructor");
		this.componentSource = componentSource;
		this.response = response;
		this.applicationStateManager = asm;
		this.logger = logger;
		this.linkSource = linkSource;
	}

	@Override
	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {
		logger.debug("handleComponentEvent entered");
		boolean accessOK = checkAccess(parameters.getActivePageName());
		if (accessOK) {
			logger.debug("handleComponentEvent access granted");
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
		logger.debug("handlePageRender entered");
		boolean accessOK = checkAccess(parameters.getLogicalPageName());
		if (accessOK) {
			logger.debug("handlePageRender access denied");
			handler.handlePageRender(parameters);
		} else {
			logger.error("handlePageRender: ACCESS DENIED TO {}", parameters.getLogicalPageName());
			response.sendRedirect(linkSource.createPageRenderLink(Index.class));
		}
	}

	public boolean checkAccess(String pageName) throws IOException {
		boolean hasAccessAnnotation = false;
		logger.debug("checkAccess: page {}", pageName);
		if (pageName.equals("") || pageName.equals("/")) {
			pageName = AppConstants.PageIndex;
		}

		Component page = null;
		page = componentSource.getPage(pageName);

		boolean publicPage = page.getClass().isAnnotationPresent(PublicPage.class);
		boolean studentPage = page.getClass().isAnnotationPresent(StudentPage.class);
		boolean instructorPage = page.getClass().isAnnotationPresent(InstructorPage.class);
		boolean adminPage = page.getClass().isAnnotationPresent(AdministratorPage.class);

		hasAccessAnnotation = publicPage | studentPage | instructorPage | adminPage;
		logger.debug("checkAccess: page has access annotation: {}", hasAccessAnnotation);

		boolean canAccess = false;
		if (publicPage) {
			logger.debug("checkAccess: Accessing a public page.");
			canAccess = true;
		} else {
			logger.debug("checkAccess: Accessing a not for public page.");
			UserInfo userInfo = applicationStateManager.getIfExists(UserInfo.class);
			if (userInfo == null) {
				logger.debug("checkAccess: UserInfo is null");
			} else {
				logger.debug("checkAccess: userInfo is not null");
				if (studentPage) {
					logger.debug("checkAccess: studentPage");
					canAccess = canAccess || userInfo.isStudent();
				}
				if (instructorPage) {
					logger.debug("checkAccess: instructorPage");
					canAccess = canAccess || userInfo.isInstructor();
				}
				if (adminPage) {
					logger.debug("checkAccess: adminPage");
					canAccess = canAccess || userInfo.isAdministrator();
				}
			}
		}

		if (canAccess && hasAccessAnnotation) {
			logger.debug("checkAccess: ACCESS GRANTED to page:{} canaccess:{} hasaccessannotation:{} ", pageName,
					canAccess, hasAccessAnnotation);
			return true;
		} else {
			logger.info("checkAccess: ACCESS DENIED to page:{} canaccess:{} hasaccessannotation:{} ", pageName,
					canAccess, hasAccessAnnotation);
			return false;
		}
	}

}
