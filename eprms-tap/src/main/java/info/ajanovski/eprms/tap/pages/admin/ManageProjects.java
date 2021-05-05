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

package info.ajanovski.eprms.tap.pages.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.Team;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class ManageProjects {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private ProjectManager projectManager;

	@Inject
	private GenericService genericService;

	public List<Project> getAllProjects() {
		return (List<Project>) projectManager.getAllProjectsOrderByTitle();
	}

	public List<Project> getProjects() {
		if (selectedProject != null) {
			List<Project> ls = new ArrayList<Project>();
			ls.add(genericService.getByPK(Project.class, selectedProject.getProjectId()));
			return ls;
		} else {
			return getAllProjects();
		}
	}

	@Property
	private Project project;

	@Persist
	@Property
	private Project selectedProject;

	@Property
	private Responsibility responsibility;

	@Property
	private TeamMember teamMember;

	@Property
	private Database database;

	@Property
	private Repository repository;

	@Persist
	@Property
	private TeamMember newTm;

	@Persist
	@Property
	private Project newProject;

	@Persist
	@Property
	private Team newTeam;

	@Persist
	@Property
	private Responsibility newResponsibility;

	@Persist
	@Property
	private Database newDb;

	@Persist
	@Property
	private Repository newRp;

	public void onActionFromAddTeamMember(Team t) {
		newTm = new TeamMember();
		newTm.setTeam(t);
	}

	public void onActionFromNewProject() {
		newProject = new Project();
	}

	public void onActionFromEditProject(Project p) {
		newProject = p;
		inCourses = projectManager.getProjectCourses(newProject).stream().map(cp -> cp.getCourse())
				.collect(Collectors.toList());
	}

	public void onActionFromNewTeam(Project p) {
		newTeam = new Team();
		newResponsibility = new Responsibility();
		newResponsibility.setProject(p);
		newResponsibility.setTeam(newTeam);
	}

	public void onActionFromNewDatabase(Project p) {
		newDb = new Database();
		newDb.setProject(p);
	}

	public void onActionFromNewRepository(Project p) {
		newRp = new Repository();
		newRp.setProject(p);
	}

	public List<Course> getAllCourses() {
		return (List<Course>) genericService.getAll(Course.class);
	}

	@Persist
	@Property
	private List<Course> inCourses;

	@Inject
	private Messages messages;

	public SelectModel getCoursesModel() {
		return selectModelFactory.create(getAllCourses(), "title");
	}

	public SelectModel getSelectedCoursesModel() {
		return selectModelFactory.create(inCourses, "title");
	}

	public ValueEncoder<Course> getCourseEncoder() {
		return new ValueEncoder<Course>() {
			@Override
			public String toClient(Course value) {
				return String.valueOf(value.getCourseId());
			}

			@Override
			public Course toValue(String id) {
				return genericService.getByPK(Course.class, Long.parseLong(id));
			}
		};
	}

	@CommitAfter
	public void onSuccessFromTeamMemberForm() {
		genericService.save(newTm);
		newTm = null;
	}

	@CommitAfter
	public void onSuccessFromNewProjectForm() {
		genericService.saveOrUpdate(newProject);
		projectManager.addCoursesToProject(inCourses, newProject);
		selectedProject = newProject;
		newProject = null;
	}

	@CommitAfter
	public void onSuccessFromNewTeamForm() {
		genericService.save(newTeam);
		genericService.save(newResponsibility);
		newTeam = null;
		newResponsibility = null;
	}

	@CommitAfter
	public void onSuccessFromNewDatabaseForm() {
		genericService.save(newDb);
		newDb = null;
	}

	@CommitAfter
	public void onSuccessFromNewRepositoryForm() {
		genericService.save(newRp);
		newRp = null;
	}

	@Property
	private Person person;

	public List<Person> getPersons() {
		return (List<Person>) genericService.getAll(Person.class);
	}

	@Inject
	private SelectModelFactory selectModelFactory;

	public SelectModel getPersonModel() {
		return (SelectModel) selectModelFactory.create(getPersons(), "userName");
	}

	public SelectModel getProjectModel() {
		return (SelectModel) selectModelFactory.create(getAllProjects(), "title");
	}

	@Property
	private Course course;

	public List<Course> getProjectCourses() {
		return projectManager.getProjectCourses(project).stream().map(cp -> cp.getCourse())
				.collect(Collectors.toList());
	}

	@Persist
	@Property
	private Project copyActivitiesFromProject;

	public void onActionFromCopyActivities(Project p) {
		copyActivitiesFromProject = p;
	}

	public void onActionFromResetClipboard() {
		copyActivitiesFromProject = null;
	}

	@CommitAfter
	public void onActionFromPasteActivities(Project pasteActivitiesToProject) {
		for (Activity original : genericService.getByPK(Project.class, copyActivitiesFromProject.getProjectId())
				.getActivities()) {
			Activity copy = new Activity();
			copy.setDescription(original.getDescription());
			copy.setTitle(original.getTitle());
			copy.setProject(pasteActivitiesToProject);
			copy.setActivityType(original.getActivityType());
			genericService.save(copy);
		}
		genericService.save(pasteActivitiesToProject);
		copyActivitiesFromProject = null;
	}

}
