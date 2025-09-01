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
Denotes which CourseProjects are open for discussions in the selected DiscussionSession
*/
@Entity
@Table (schema="eprms_main", name="discussion_on_course_project")
public class DiscussionOnCourseProject implements java.io.Serializable {
	private long discussionOnCourseProjectId;
	private DiscussionSession discussionSession;
	private CourseProject courseProject;
	private List<DiscussionPost> discussionPosts = new ArrayList<DiscussionPost>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "discussion_on_course_project_id", unique = true, nullable = false)
	public long getDiscussionOnCourseProjectId() {
		return this.discussionOnCourseProjectId;
	}

	public void setDiscussionOnCourseProjectId(long discussionOnCourseProjectId) {
		this.discussionOnCourseProjectId=discussionOnCourseProjectId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discussion_session_id", nullable = false, foreignKey = @ForeignKey(name = "fk_discussion_on_course_project_discussion_session"))
	public DiscussionSession getDiscussionSession() {
		return this.discussionSession;
	}

	public void setDiscussionSession(DiscussionSession discussionSession) {
		this.discussionSession=discussionSession;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_project_id", nullable = true, foreignKey = @ForeignKey(name = "fk_discussion_on_course_project_course_project"))
	public CourseProject getCourseProject() {
		return this.courseProject;
	}

	public void setCourseProject(CourseProject courseProject) {
		this.courseProject=courseProject;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "discussionOnCourseProject")
	public List<DiscussionPost> getDiscussionPosts() {
		return this.discussionPosts;
	}

	public void setDiscussionPosts(List<DiscussionPost> discussionPosts) {
		this.discussionPosts=discussionPosts;
	}

}
