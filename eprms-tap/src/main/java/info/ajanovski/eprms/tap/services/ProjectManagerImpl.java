/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource 
 * Management System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *     
 * EPRMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 ******************************************************************************/

package info.ajanovski.eprms.tap.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.CourseProject;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.model.util.ProjectCodeComparator;
import info.ajanovski.eprms.model.util.WorkEvaluationComparator;
import info.ajanovski.eprms.model.util.WorkReportComparator;
import info.ajanovski.eprms.tap.data.ProjectDao;
import info.ajanovski.eprms.tap.util.AppConstants;

public class ProjectManagerImpl implements ProjectManager {

	@Inject
	private ProjectDao projectDao;

	@Inject
	private GenericService genericService;

	@Inject
	private SystemConfigService systemConfigService;

	@Override
	public List<Project> getAllProjects() {
		return projectDao.getAllProjects();
	}

	@Override
	public List<CourseProject> getProjectCourses(Project p) {
		return projectDao.getProjectCourses(p);
	}

	@Override
	public void addCoursesToProject(List<Course> inCourses, Project p) {
		List<CourseProject> projectCourses = projectDao.getProjectCourses(p);
		List<Course> coursesFromProjectCourses = projectCourses.stream().map(cp -> cp.getCourse())
				.collect(Collectors.toList());
		for (Course c : inCourses) {
			if (!coursesFromProjectCourses.contains(c)) {
				CourseProject cp = new CourseProject();
				cp.setCourse(c);
				cp.setProject(p);
				genericService.save(cp);
			}
		}
		for (CourseProject cp : projectCourses) {
			if (!inCourses.contains(cp.getCourse())) {
				genericService.delete(cp);
			}
		}
	}

	@Override
	public Float sumPoints(Project p) {
		return projectDao.sumPoints(p);
	}

	@Override
	public List<Project> getAllProjectsInCourse(Course selectedCourse) {
		return projectDao.getAllProjectsInCourse(selectedCourse);
	}

	@Override
	public List<Project> getProjectByPerson(Long personId) {
		return projectDao.getProjectByPerson(personId);
	}

	@Override
	public void cycleStatus(Project p) {
		if (p.getStatus() != null) {
			String s = p.getStatus();
			int index = Arrays.asList(ModelConstants.AllProjectStatuses).indexOf(s);
			index++;
			if (index < 0) {
				index = 0;
			} else if (index >= ModelConstants.AllProjectStatuses.length) {
				index = 0;
			} else {
			}
			p.setStatus(Arrays.asList(ModelConstants.AllProjectStatuses).get(index));
		} else {
			p.setStatus(ModelConstants.AllProjectStatuses[0]);
		}
	}

	@Override
	public List<WorkEvaluation> getWorkEvaluationForWorkReport(WorkReport workReport) {
		List<WorkEvaluation> list = workReport.getWorkEvaluations();
		WorkEvaluationComparator comparator = new WorkEvaluationComparator();
		Collections.sort(list, comparator);
		return list;
	}

	@Override
	public List<WorkReport> getWorkReportsForActivity(Activity activity) {
		List<WorkReport> list;
		if (activity != null) {
			list = activity.getWorkReports();
		} else {
			list = new ArrayList<WorkReport>();
		}
		WorkReportComparator comparator = new WorkReportComparator();
		Collections.sort(list, comparator);
		return list;
	}

	@Override
	public List<TeamMember> getTeamMembershipOfPerson(Long personId) {
		return projectDao.getTeamMembershipOfPerson(personId);
	}

	@Override
	public String getProjectURL(Project project) {
		String baseUrl = systemConfigService.getString(AppConstants.SystemParameterPMProjectURLPrefix);
		if (project.getCode() != null) {
			return (baseUrl == null ? "" : baseUrl) + project.getCode();
		} else {
			return "";
		}
	}

	@Override
	public List<Project> orderProjectList(List<Project> list) {
		ProjectCodeComparator pcc = new ProjectCodeComparator();
		Collections.sort(list, pcc);
		return list;
	}

}
