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

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.entities.Database;
import info.ajanovski.eprms.tap.entities.Person;
import info.ajanovski.eprms.tap.entities.Project;
import info.ajanovski.eprms.tap.entities.Repository;
import info.ajanovski.eprms.tap.entities.Responsibility;
import info.ajanovski.eprms.tap.entities.Team;
import info.ajanovski.eprms.tap.entities.TeamMember;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class Projects {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	public List<Project> getAllProjects() {
		return (List<Project>) genericService.getAll(Project.class);
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

	@CommitAfter
	public void onSuccessFromTeamMemberForm() {
		genericService.save(newTm);
		newTm = null;
	}

	@CommitAfter
	public void onSuccessFromNewProjectForm() {
		genericService.save(newProject);
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

}
