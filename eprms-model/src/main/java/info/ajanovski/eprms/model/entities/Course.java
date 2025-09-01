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
@Table (schema="eprms_main", name="course")
public class Course implements java.io.Serializable {
	private long courseId;
	private String title;
	private String code;
	private List<CourseActivityType> courseActivityTypes = new ArrayList<CourseActivityType>();
	private List<CourseProject> courseProjects = new ArrayList<CourseProject>();
	private List<CourseTeacher> courseTeachers = new ArrayList<CourseTeacher>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "course_id", unique = true, nullable = false)
	public long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId=courseId;
	}

	@NotNull
	@Column(name = "title", unique = true, nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	@NotNull
	@Column(name = "code", unique = true, nullable = false)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	public List<CourseActivityType> getCourseActivityTypes() {
		return this.courseActivityTypes;
	}

	public void setCourseActivityTypes(List<CourseActivityType> courseActivityTypes) {
		this.courseActivityTypes=courseActivityTypes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	public List<CourseProject> getCourseProjects() {
		return this.courseProjects;
	}

	public void setCourseProjects(List<CourseProject> courseProjects) {
		this.courseProjects=courseProjects;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	public List<CourseTeacher> getCourseTeachers() {
		return this.courseTeachers;
	}

	public void setCourseTeachers(List<CourseTeacher> courseTeachers) {
		this.courseTeachers=courseTeachers;
	}

}
