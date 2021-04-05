/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource Management 
 * System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *     
 * EPRMS is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *     
 * You should have received a copy of the GNU General Public License along 
 * with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 * 
 ******************************************************************************/

package info.ajanovski.eprms.tap.entities;

import java.util.*;
import javax.persistence.*;

/*
*/
@Entity
@Table (schema="epm_main", name="team_member")
public class TeamMember implements java.io.Serializable {
	private long teamMemberId;
	private Long positionNumber;
	private String role;
	private Person person;
	private Team team;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "team_member_id", unique = true, nullable = false)
	public long getTeamMemberId() {
		return this.teamMemberId;
	}

	public void setTeamMemberId(long teamMemberId) {
		this.teamMemberId=teamMemberId;
	}

	@Column(name = "position_number")
	public Long getPositionNumber() {
		return this.positionNumber;
	}

	public void setPositionNumber(Long positionNumber) {
		this.positionNumber=positionNumber;
	}

	@Column(name = "role")
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role=role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_member_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person=person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_member_team"))
	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team=team;
	}

}
