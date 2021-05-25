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
@Table (schema="", name="course_activity_type")
public class CourseActivityType implements java.io.Serializable {
	private long courseActivityTypeId;
	private int order;
	private ActivityType activityType;
	private Course course;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "course_activity_type_id", unique = true, nullable = false)
	public long getCourseActivityTypeId() {
		return this.courseActivityTypeId;
	}

	public void setCourseActivityTypeId(long courseActivityTypeId) {
		this.courseActivityTypeId=courseActivityTypeId;
	}

	@Column(name = "order", nullable = false)
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order=order;
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
