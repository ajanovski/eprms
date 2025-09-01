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
@Table (schema="eprms_main", name="course_teacher")
public class CourseTeacher implements java.io.Serializable {
	private long courseTeacherId;
	private Date fromDate;
	private Date toDate;
	private Person teacher;
	private Course course;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "course_teacher_id", unique = true, nullable = false)
	public long getCourseTeacherId() {
		return this.courseTeacherId;
	}

	public void setCourseTeacherId(long courseTeacherId) {
		this.courseTeacherId=courseTeacherId;
	}

	@Column(name = "from_date")
	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate=fromDate;
	}

	@Column(name = "to_date")
	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate=toDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_teacher_person"))
	public Person getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Person teacher) {
		this.teacher=teacher;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_teacher_course"))
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course=course;
	}

}
