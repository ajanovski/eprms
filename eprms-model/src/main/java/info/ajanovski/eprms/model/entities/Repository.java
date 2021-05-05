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

/*
*/
@Entity
@Table (schema="epm_main", name="repository")
public class Repository implements java.io.Serializable {
	private long repositoryId;
	private String title;
	private String url;
	private Date dateCreated;
	private String type;
	private Person person;
	private Team team;
	private Project project;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "repository_id", unique = true, nullable = false)
	public long getRepositoryId() {
		return this.repositoryId;
	}

	public void setRepositoryId(long repositoryId) {
		this.repositoryId=repositoryId;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	@Column(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url=url;
	}

	@Column(name = "date_created")
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated=dateCreated;
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type=type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = true, foreignKey = @ForeignKey(name = "fk_repository_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person=person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id", nullable = true, foreignKey = @ForeignKey(name = "fk_repository_team"))
	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team=team;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = true, foreignKey = @ForeignKey(name = "fk_repository_Project"))
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project=project;
	}

}
