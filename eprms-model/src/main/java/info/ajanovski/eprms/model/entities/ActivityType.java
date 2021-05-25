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
@Table (schema="epm_main", name="activity_type")
public class ActivityType implements java.io.Serializable {
	private long activityTypeId;
	private String title;
	private String description;
	private String code;
	private ActivityType superActivityType;
	private List<ActivityType> subActivityTypes = new ArrayList<ActivityType>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "activity_type_id", unique = true, nullable = false)
	public long getActivityTypeId() {
		return this.activityTypeId;
	}

	public void setActivityTypeId(long activityTypeId) {
		this.activityTypeId=activityTypeId;
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

	@Column(name = "code", unique = true, nullable = false)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_type_id", nullable = true, foreignKey = @ForeignKey(name = "fk_activity_type_activity_type"))
	public ActivityType getSuperActivityType() {
		return this.superActivityType;
	}

	public void setSuperActivityType(ActivityType superActivityType) {
		this.superActivityType=superActivityType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "superActivityType")
	public List<ActivityType> getSubActivityTypes() {
		return this.subActivityTypes;
	}

	public void setSubActivityTypes(List<ActivityType> subActivityTypes) {
		this.subActivityTypes=subActivityTypes;
	}

}
