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
@Table(schema = "epm_main", name = "discussion_post_evaluation")
public class DiscussionPostEvaluation implements java.io.Serializable {
	private long discussionPostEvaluationId;
	private String type;
	private String message;
	private Integer points;
	private Date evaluatedOn;
	private Boolean accepted;
	private Boolean evaluatePostingAsATeam;
	private Person person;
	private DiscussionPost discussionPost;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "discussion_post_evaluation_id", unique = true, nullable = false)
	public long getDiscussionPostEvaluationId() {
		return this.discussionPostEvaluationId;
	}

	public void setDiscussionPostEvaluationId(long discussionPostEvaluationId) {
		this.discussionPostEvaluationId = discussionPostEvaluationId;
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

	@Column(name = "points")
	public Integer getPoints() {
		return this.points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@NotNull
	@Column(name = "evaluated_on", nullable = false)
	public Date getEvaluatedOn() {
		return this.evaluatedOn;
	}

	public void setEvaluatedOn(Date evaluatedOn) {
		this.evaluatedOn = evaluatedOn;
	}

	@Column(name = "accepted")
	public Boolean getAccepted() {
		return this.accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	@Column(name = "evaluate_posting_as_a_team")
	public Boolean getEvaluatePostingAsATeam() {
		return this.evaluatePostingAsATeam;
	}

	public void setEvaluatePostingAsATeam(Boolean evaluatePostingAsATeam) {
		this.evaluatePostingAsATeam = evaluatePostingAsATeam;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_discussion_post_evaluation_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discussion_post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_discussion_post_evaluation_discussion_post"))
	public DiscussionPost getDiscussionPost() {
		return this.discussionPost;
	}

	public void setDiscussionPost(DiscussionPost discussionPost) {
		this.discussionPost = discussionPost;
	}

}
