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
@Table (schema="epm_main", name="course_project")
public class CourseProject implements java.io.Serializable {
	private long courseProjectId;
	private Course course;
	private Project project;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "course_project_id", unique = true, nullable = false)
	public long getCourseProjectId() {
		return this.courseProjectId;
	}

	public void setCourseProjectId(long courseProjectId) {
		this.courseProjectId=courseProjectId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_project_course"))
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course=course;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_project_Project"))
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project=project;
	}

}
