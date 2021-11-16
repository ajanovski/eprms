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

package info.ajanovski.eprms.tap.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.beanmodel.services.PropertyConduitSource;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Team;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

public class TeamMembersGrid {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.PROP)
	private Team team;

	@Property
	private TeamMember teamMember;

	@CommitAfter
	public void onActionFromDeleteTeamMember(TeamMember tm) {
		genericService.delete(tm);
	}

	public List<TeamMember> getTeamMembers() {
		return team.getTeamMembers();
	}

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private PropertyConduitSource pcs;

	@Inject
	private Messages messages;

	public BeanModel<TeamMember> getBeanModel() {
		BeanModel<TeamMember> bm = beanModelSource.createDisplayModel(TeamMember.class, messages);
		bm.exclude("teamMemberId");
		bm.add("userName", pcs.create(TeamMember.class, "person.userName"));
		bm.add("lastName", pcs.create(TeamMember.class, "person.lastName"));
		bm.add("firstName", pcs.create(TeamMember.class, "person.firstName"));
		bm.reorder("positionNumber", "role", "lastName", "firstName", "userName");
		bm.add("actions", null);
		return bm;
	}

}
