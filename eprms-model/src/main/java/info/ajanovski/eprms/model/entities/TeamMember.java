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

package info.ajanovski.eprms.model.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

/*
*/
@Entity
@Table (schema="epm_main", name="team_member")
public class TeamMember implements java.io.Serializable {
	private long teamMemberId;
	private Integer positionNumber;
	private String role;
	private String status;
	private Date createdDate;
	private Date statusDate;
	private Person person;
	private Team team;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "team_member_id", unique = true, nullable = false)
	public long getTeamMemberId() {
		return this.teamMemberId;
	}

	public void setTeamMemberId(long teamMemberId) {
		this.teamMemberId=teamMemberId;
	}

	@Column(name = "position_number")
	public Integer getPositionNumber() {
		return this.positionNumber;
	}

	public void setPositionNumber(Integer positionNumber) {
		this.positionNumber=positionNumber;
	}

	@Column(name = "role")
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role=role;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status=status;
	}

	@Column(name = "created_date")
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate=createdDate;
	}

	@Column(name = "status_date")
	public Date getStatusDate() {
		return this.statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate=statusDate;
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
