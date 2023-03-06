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
 * 
 ******************************************************************************/

package info.ajanovski.eprms.tap.data;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;

import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.CourseProject;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.TeamMember;

public class ProjectDaoImpl implements ProjectDao {

	@Inject
	private Session session;

	private Session getEntityManager() {
		return session.getSession();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getAllProjectsOrderByTitle() {
		return getEntityManager().createQuery("from Project order by lower(title)").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CourseProject> getProjectCourses(Project p) {
		if (p != null) {
			return getEntityManager().createQuery("from CourseProject cp where cp.project.projectId=:projectId")
					.setParameter("projectId", p.getProjectId()).getResultList();
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Float sumPoints(Project p) {
		if (p != null) {
			List<Float> lista = getEntityManager().createQuery("""
								select max(we.points) as maxpoints
								from WorkEvaluation as we
								join we.workReport as wr
								join wr.activity as a
								join a.project as p
								where p.projectId=:projectId
								group by a
					""").setParameter("projectId", p.getProjectId()).getResultList();
			Float output = 0.0f;
			for (Float o : lista) {
				if (o != null) {
					output += o;
				}
			}
			return output;
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getAllProjectsInCourseOrderByTitle(Course selectedCourse) {
		if (selectedCourse != null) {
			return getEntityManager().createQuery("""
					select p
					from CourseProject cp
					join cp.project p
					join cp.course c
					where c.courseId=:courseId
					order by lower(p.title)
					""").setParameter("courseId", selectedCourse.getCourseId()).getResultList();
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getProjectByPerson(Long personId) {
		if (personId != null) {
			return getEntityManager().createQuery("""
					select p
					from Project p
					join p.responsibilities r
					join r.team t
					join t.teamMembers tm
					join tm.person person
					where person.personId=:personId
					order by p.title
					""").setParameter("personId", personId).getResultList();
		} else {
			return null;
		}
	}

	@Override
	public List<TeamMember> getTeamMembershipOfPerson(Long personId) {
		return getEntityManager().createQuery("""
				select tm
				from Team t
				join t.teamMembers tm
				join tm.person p
				where p.personId=:personId
				""").setParameter("personId", personId).getResultList();
	}

}
