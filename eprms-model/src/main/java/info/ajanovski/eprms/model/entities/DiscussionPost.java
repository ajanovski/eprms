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
@Table(schema = "epm_main", name = "discussion_post")
public class DiscussionPost implements java.io.Serializable {
	private long discussionPostId;
	private String type;
	private String message;
	private Date postedOn;
	private boolean publicPosting;
	private List<DiscussionPost> replies = new ArrayList<DiscussionPost>();
	private DiscussionPost replyTo;
	private DiscussionOnCourseProject discussionOnCourseProject;
	private Person person;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@NotNull
	@Column(name = "discussion_post_id", unique = true, nullable = false)
	public long getDiscussionPostId() {
		return this.discussionPostId;
	}

	public void setDiscussionPostId(long discussionPostId) {
		this.discussionPostId = discussionPostId;
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@NotNull
	@Column(name = "message", nullable = false, length = 100000)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@NotNull
	@Column(name = "posted_on", nullable = false)
	public Date getPostedOn() {
		return this.postedOn;
	}

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@NotNull
	@Column(name = "public_posting", nullable = false)
	public boolean getPublicPosting() {
		return this.publicPosting;
	}

	public void setPublicPosting(boolean publicPosting) {
		this.publicPosting = publicPosting;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "replyTo")
	public List<DiscussionPost> getReplies() {
		return this.replies;
	}

	public void setReplies(List<DiscussionPost> replies) {
		this.replies = replies;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_to_discussion_post_id", nullable = true, foreignKey = @ForeignKey(name = "fk_discussion_post_discussion_post"))
	public DiscussionPost getReplyTo() {
		return this.replyTo;
	}

	public void setReplyTo(DiscussionPost replyTo) {
		this.replyTo = replyTo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discussion_on_course_project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_discussion_post_discussion_on_course_project"))
	public DiscussionOnCourseProject getDiscussionOnCourseProject() {
		return this.discussionOnCourseProject;
	}

	public void setDiscussionOnCourseProject(DiscussionOnCourseProject discussionOnCourseProject) {
		this.discussionOnCourseProject = discussionOnCourseProject;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_discussion_post_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
