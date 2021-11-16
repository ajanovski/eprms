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
@Table (schema="epm_main", name="work_report")
public class WorkReport implements java.io.Serializable {
	private long workReportId;
	private String title;
	private String description;
	private Float percentReported;
	private Date submissionDate;
	private Activity activity;
	private Person person;
	private WorkReport continuationOfWorkReport;
	private Team team;
	private List<WorkEvaluation> workEvaluations = new ArrayList<WorkEvaluation>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "work_report_id", unique = true, nullable = false)
	public long getWorkReportId() {
		return this.workReportId;
	}

	public void setWorkReportId(long workReportId) {
		this.workReportId=workReportId;
	}

	@Column(name = "title", length = 4000)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	@Column(name = "description", length = 1000000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description=description;
	}

	@Column(name = "percent_reported")
	public Float getPercentReported() {
		return this.percentReported;
	}

	public void setPercentReported(Float percentReported) {
		this.percentReported=percentReported;
	}

	@Column(name = "submission_date")
	public Date getSubmissionDate() {
		return this.submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate=submissionDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_report_activity"))
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity=activity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_report_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person=person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "continuation_of_work_report_id", nullable = true, foreignKey = @ForeignKey(name = "fk_work_report_work_report"))
	public WorkReport getContinuationOfWorkReport() {
		return this.continuationOfWorkReport;
	}

	public void setContinuationOfWorkReport(WorkReport continuationOfWorkReport) {
		this.continuationOfWorkReport=continuationOfWorkReport;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id", nullable = true, foreignKey = @ForeignKey(name = "fk_work_report_team"))
	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team=team;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workReport")
	public List<WorkEvaluation> getWorkEvaluations() {
		return this.workEvaluations;
	}

	public void setWorkEvaluations(List<WorkEvaluation> workEvaluations) {
		this.workEvaluations=workEvaluations;
	}

}
