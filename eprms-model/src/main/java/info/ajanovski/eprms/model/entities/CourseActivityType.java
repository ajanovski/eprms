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
@Table (schema="epm_main", name="course_activity_type")
public class CourseActivityType implements java.io.Serializable {
	private long courseActivityTypeId;
	private int positionNumber;
	private ActivityType activityType;
	private Course course;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "course_activity_type_id", unique = true, nullable = false)
	public long getCourseActivityTypeId() {
		return this.courseActivityTypeId;
	}

	public void setCourseActivityTypeId(long courseActivityTypeId) {
		this.courseActivityTypeId=courseActivityTypeId;
	}

	@NotNull
	@Column(name = "position_number", nullable = false)
	public int getPositionNumber() {
		return this.positionNumber;
	}

	public void setPositionNumber(int positionNumber) {
		this.positionNumber=positionNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_activity_type_activity_type"))
	public ActivityType getActivityType() {
		return this.activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType=activityType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_activity_type_course"))
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course=course;
	}

}
