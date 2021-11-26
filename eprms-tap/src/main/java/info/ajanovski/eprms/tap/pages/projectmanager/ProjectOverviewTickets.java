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

package info.ajanovski.eprms.tap.pages.projectmanager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.util.AppConstants;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class ProjectOverviewTickets {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private SystemConfigService systemConfigService;

	@Property
	@Persist
	private String tickets;

	@Property
	@Persist
	private Date lastUpdate;

	public void onActivate() {
		LocalDateTime now = LocalDateTime.now().minus(Duration.of(1, ChronoUnit.MINUTES));
		Date nowMinus5 = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

		if ((tickets == null && lastUpdate == null) || (lastUpdate != null && lastUpdate.compareTo(nowMinus5) < 0)) {
			try {
				String url = systemConfigService.getString(AppConstants.SystemParameterPMOverviewTicketsURL);
				HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
				HttpClient client = HttpClient.newBuilder().build();
				HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
				tickets = response.body();
				lastUpdate = new Date();
			} catch (Exception e) {
				tickets = null;
				lastUpdate = null;
			}
		}
	}
}
