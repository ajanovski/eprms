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
@Table (schema="epm_main", name="work_evaluation")
public class WorkEvaluation implements java.io.Serializable {
	private long workEvaluationId;
	private String title;
	private String description;
	private Float percentEvaluated;
	private Float points;
	private Date evaluationDate;
	private String status;
	private WorkReport workReport;
	private Person person;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@NotNull
	@Column(name = "work_evaluation_id", unique = true, nullable = false)
	public long getWorkEvaluationId() {
		return this.workEvaluationId;
	}

	public void setWorkEvaluationId(long workEvaluationId) {
		this.workEvaluationId=workEvaluationId;
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

	@Column(name = "percent_evaluated")
	public Float getPercentEvaluated() {
		return this.percentEvaluated;
	}

	public void setPercentEvaluated(Float percentEvaluated) {
		this.percentEvaluated=percentEvaluated;
	}

	@Column(name = "points")
	public Float getPoints() {
		return this.points;
	}

	public void setPoints(Float points) {
		this.points=points;
	}

	@Column(name = "evaluation_date")
	public Date getEvaluationDate() {
		return this.evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate=evaluationDate;
	}

	@NotNull
	@Column(name = "status", nullable = false)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_report_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_evaluation_work_report"))
	public WorkReport getWorkReport() {
		return this.workReport;
	}

	public void setWorkReport(WorkReport workReport) {
		this.workReport=workReport;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_evaluation_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person=person;
	}

}
