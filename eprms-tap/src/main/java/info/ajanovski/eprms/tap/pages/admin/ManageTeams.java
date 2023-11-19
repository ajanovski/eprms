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

package info.ajanovski.eprms.tap.pages.admin;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.Team;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class ManageTeams {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Property
	private Team team;

	@Property
	private TeamMember teamMember;

	@Persist
	@Property
	private Boolean approvalOnly;

	@Property
	private Responsibility responsibility;

	public void onActivate() {
		if (approvalOnly == null) {
			approvalOnly = true;
		}
	}

	public List<Team> getTeams() {
		List<Team> lista = (List<Team>) genericService.getAll(Team.class);
		if (approvalOnly != null && approvalOnly) {
			return lista.stream()
					.filter(p -> p.getStatus() != null && p.getStatus().equals(ModelConstants.TeamStatusProposed))
					.toList();
		} else {
			return lista;
		}
	}

	void onActionFromToggleApprovalOnly() {
		if (approvalOnly == null) {
			approvalOnly = false;
		} else {
			approvalOnly = !approvalOnly;
		}
	}

	@CommitAfter
	void onActionFromApproveTeam(Team t) {
		t.setStatus(ModelConstants.TeamStatusAccepted);
		t.setOpenForNewMembers(false);
		t.setStatusDate(new Date());
		for (TeamMember tm : t.getTeamMembers()) {
			tm.setStatus(ModelConstants.TeamMemberStatusAccepted);
			genericService.saveOrUpdate(tm);
		}
		genericService.saveOrUpdate(t);
	}

	@CommitAfter
	void onActionFromRemoveTeam(Team t) {
		for (TeamMember tm : t.getTeamMembers()) {
			genericService.delete(tm);
		}
		genericService.delete(t);
	}

	public List<Responsibility> getTeamResponsibilities() {
		return team.getResponsibilities();
	}

}
