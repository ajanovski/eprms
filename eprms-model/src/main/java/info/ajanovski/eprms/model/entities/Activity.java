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
@Table (schema="epm_main", name="activity")
public class Activity implements java.io.Serializable {
	private long activityId;
	private String title;
	private String description;
	private Activity parentActivity;
	private ActivityType activityType;
	private Project project;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "activity_id", unique = true, nullable = false)
	public long getActivityId() {
		return this.activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId=activityId;
	}

	@Column(name = "title")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_activity_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_activity"))
	public Activity getParentActivity() {
		return this.parentActivity;
	}

	public void setParentActivity(Activity parentActivity) {
		this.parentActivity=parentActivity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_activity_type"))
	public ActivityType getActivityType() {
		return this.activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType=activityType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_Project"))
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project=project;
	}

}
