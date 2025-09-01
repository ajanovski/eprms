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
@Table (schema="eprms_main", name="Project")
public class Project implements java.io.Serializable {
	private long projectId;
	private String title;
	private String description;
	private Date startDate;
	private Date finishDate;
	private String code;
	private String status;
	private Boolean acceptingNewResponsibilities;
	private Integer maxResponsibilities;
	private List<Responsibility> responsibilities = new ArrayList<Responsibility>();
	private List<Repository> repositories = new ArrayList<Repository>();
	private List<Database> databases = new ArrayList<Database>();
	private List<Activity> activities = new ArrayList<Activity>();
	private List<CourseProject> courseProjects = new ArrayList<CourseProject>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "project_id", unique = true, nullable = false)
	public long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId=projectId;
	}

	@NotNull
	@Column(name = "title", unique = true, nullable = false, length = 4000)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	@Column(name = "description", length = 1000000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description=description;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate=startDate;
	}

	@Column(name = "finish_date")
	public Date getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate=finishDate;
	}

	@NotNull
	@Column(name = "code", nullable = false)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status=status;
	}

	@Column(name = "accepting_new_responsibilities")
	public Boolean getAcceptingNewResponsibilities() {
		return this.acceptingNewResponsibilities;
	}

	public void setAcceptingNewResponsibilities(Boolean acceptingNewResponsibilities) {
		this.acceptingNewResponsibilities=acceptingNewResponsibilities;
	}

	@Column(name = "max_responsibilities")
	public Integer getMaxResponsibilities() {
		return this.maxResponsibilities;
	}

	public void setMaxResponsibilities(Integer maxResponsibilities) {
		this.maxResponsibilities=maxResponsibilities;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public List<CourseProject> getCourseProjects() {
		return this.courseProjects;
	}

	public void setCourseProjects(List<CourseProject> courseProjects) {
		this.courseProjects=courseProjects;
	}

}
