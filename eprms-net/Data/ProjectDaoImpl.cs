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

using System.Collections.Generic;
using info.ajanovski.eprms.net.Models;
using Microsoft.Extensions.Logging;
using System.Linq;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;

namespace info.ajanovski.eprms.net.Data {
    public class ProjectDaoImpl : ProjectDao {


        private readonly EPRMSNetDbContext session;

        private readonly ILogger<ProjectDaoImpl> logger;

        public ProjectDaoImpl(EPRMSNetDbContext session, ILogger<ProjectDaoImpl> logger) {
            this.session = session;
            this.logger = logger;
        }


        public List<Project> getAllProjectsOrderByTitle() {
            return session.Projects.OrderBy(p => p.Title).ToList();
        }

        public List<CourseProject> getProjectCourses(Project p) {
            if (p != null) {
                return session.CourseProjects.Where(cp => cp.Project.ProjectId == p.ProjectId).ToList();
            } else {
                return null;
            }
        }

        public List<Project> getCourseProjectsOrderByTitle(Course selectedCourse) {
            if (selectedCourse != null) {
                return session.Projects.FromSqlRaw(@"
					select p
					from Project p
					join p.courseProjects cp
					join cp.course c
					where c.courseId=@courseId
					order by p.title
					", new SqlParameter("@courseId", selectedCourse.CourseId)).ToList();
            } else {
                return null;
            }
        }

        public List<Project> getProjectByPerson(long? personId) {
            if (personId != null) {
                return session.Projects.FromSqlRaw(@"
					select p
					from Project p
					join p.responsibilities r
					join r.team t
					join t.teamMembers tm
					join tm.person person
					where person.personId=@personId
					order by p.title
					", new SqlParameter("@personId", personId)).ToList();
            } else {
                return null;
            }
        }

        public void addCoursesToProject(List<Course> inCourses, Project p) {
            List<CourseProject> projectCourses = getProjectCourses(p);
            //List<Course> coursesFromProjectCourses = projectCourses.stream().map(cp -> cp.getCourse());
            List<Course> coursesFromProjectCourses = projectCourses.Select(cp => cp.Course).ToList();
            foreach (Course c in inCourses) {
                if (!coursesFromProjectCourses.Contains(c)) {
                    CourseProject cp = new CourseProject();
                    cp.Course = c;
                    cp.Project = p;
                    session.CourseProjects.Add(cp);
                    session.SaveChanges();
                }
            }
            foreach (CourseProject cp in projectCourses) {
                if (!inCourses.Contains(cp.Course)) {
                    session.CourseProjects.Remove(cp);
                    session.SaveChanges();
                }
            }
        }


    }
}
