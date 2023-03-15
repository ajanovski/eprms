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
@Table (schema="epm_main", name="discussion_session")
public class DiscussionSession implements java.io.Serializable {
	private long discussionSessionId;
	private Date startDate;
	private Date endDate;
	private String title;
	private List<DiscussionOnCourseProject> discussionsOnCourseProjects = new ArrayList<DiscussionOnCourseProject>();
	private Course course;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "discussion_session_id", unique = true, nullable = false)
	public long getDiscussionSessionId() {
		return this.discussionSessionId;
	}

	public void setDiscussionSessionId(long discussionSessionId) {
		this.discussionSessionId=discussionSessionId;
	}

	@NotNull
	@Column(name = "start_date", nullable = false)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate=startDate;
	}

	@NotNull
	@Column(name = "end_date", nullable = false)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate=endDate;
	}

	@NotNull
	@Column(name = "title", nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "discussionSession")
	public List<DiscussionOnCourseProject> getDiscussionsOnCourseProjects() {
		return this.discussionsOnCourseProjects;
	}

	public void setDiscussionsOnCourseProjects(List<DiscussionOnCourseProject> discussionsOnCourseProjects) {
		this.discussionsOnCourseProjects=discussionsOnCourseProjects;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = true, foreignKey = @ForeignKey(name = "fk_discussion_session_course"))
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course=course;
	}

}
