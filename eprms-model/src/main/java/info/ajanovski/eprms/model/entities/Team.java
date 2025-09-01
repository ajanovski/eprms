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
@Table (schema="eprms_main", name="team")
public class Team implements java.io.Serializable {
	private long teamId;
	private String name;
	private String status;
	private Date createdDate;
	private Date statusDate;
	private String description;
	private Boolean openForNewMembers;
	private Integer maxMembers;
	private List<TeamMember> teamMembers = new ArrayList<TeamMember>();
	private List<Responsibility> responsibilities = new ArrayList<Responsibility>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "team_id", unique = true, nullable = false)
	public long getTeamId() {
		return this.teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId=teamId;
	}

	@Column(name = "name", length = 2000)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name=name;
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

	@Column(name = "description", length = 1000000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description=description;
	}

	@Column(name = "open_for_new_members")
	public Boolean getOpenForNewMembers() {
		return this.openForNewMembers;
	}

	public void setOpenForNewMembers(Boolean openForNewMembers) {
		this.openForNewMembers=openForNewMembers;
	}

	@Column(name = "max_members")
	public Integer getMaxMembers() {
		return this.maxMembers;
	}

	public void setMaxMembers(Integer maxMembers) {
		this.maxMembers=maxMembers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
	public List<TeamMember> getTeamMembers() {
		return this.teamMembers;
	}

	public void setTeamMembers(List<TeamMember> teamMembers) {
		this.teamMembers=teamMembers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
	public List<Responsibility> getResponsibilities() {
		return this.responsibilities;
	}

	public void setResponsibilities(List<Responsibility> responsibilities) {
		this.responsibilities=responsibilities;
	}

}
