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
@Table (schema="epm_main", name="Project")
public class Project implements java.io.Serializable {
	private long projectId;
	private String title;
	private String description;
	private List<Responsibility> responsibilities = new ArrayList<Responsibility>();
	private List<Repository> repositories = new ArrayList<Repository>();
	private List<Database> databases = new ArrayList<Database>();
	private List<Activity> activities = new ArrayList<Activity>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "project_id", unique = true, nullable = false)
	public long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId=projectId;
	}

	@Column(name = "title", unique = true, nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description=description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public List<Responsibility> getResponsibilities() {
		return this.responsibilities;
	}

	public void setResponsibilities(List<Responsibility> responsibilities) {
		this.responsibilities=responsibilities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public List<Repository> getRepositories() {
		return this.repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories=repositories;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public List<Database> getDatabases() {
		return this.databases;
	}

	public void setDatabases(List<Database> databases) {
		this.databases=databases;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities=activities;
	}

}
