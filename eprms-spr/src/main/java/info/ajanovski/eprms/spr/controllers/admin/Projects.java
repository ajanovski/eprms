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

package info.ajanovski.eprms.spr.controllers.admin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import groovy.util.logging.Log4j2;
import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.Team;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.spr.services.GenericService;
import info.ajanovski.eprms.spr.services.ModelConstructor;
import info.ajanovski.eprms.spr.services.PersonManager;
import info.ajanovski.eprms.spr.util.UserInfo;

@Controller
@SessionAttributes({ "userInfo", "selectedProject" })
@Log4j2
public class Projects {
	private static final Logger logger = LoggerFactory.getLogger(Projects.class);

	@Inject
	private ModelConstructor modelOps;

	@Inject
	public HttpServletRequest request;

	@Inject
	public PersonManager personManager;

	@ModelAttribute("userInfo")
	public UserInfo userInfo() {
		return new UserInfo(request, personManager);
	}

	@ModelAttribute("selectedProject")
	public Project selectedProject() {
		return null;
	}

	@Inject
	public GenericService genericService;

	private void addProjectRelatedAttributesToModel(Model model, Project selectedProject) {
		model.addAttribute("allprojects", getAllProjects());
		model.addAttribute("selectedProject", selectedProject);
		model.addAttribute("listProjects", getProjects(selectedProject));
	}

	@GetMapping(path = { "admin/Projects" })
	public String Projects(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		return "/admin/Projects";
	}

	@GetMapping(path = { "admin/Projects.newProject" })
	public String newProject(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		model.addAttribute("newProject", new Project());
		return "/admin/Projects";
	}

	@PostMapping(path = { "admin/Projects.onSuccessFromNewProjectForm" })
	@Transactional
	public String onSuccessFromNewProjectForm(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject,
			@ModelAttribute(name = "newProject") Project newProject) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);

		Project projectToSave = new Project();
		projectToSave.setTitle(newProject.getTitle());
		projectToSave.setDescription(newProject.getDescription());
		genericService.save(projectToSave);

		addProjectRelatedAttributesToModel(model, projectToSave);
		return "redirect:/admin/Projects";
	}

	@PostMapping(path = { "admin/Projects.selectProject" })
	public String SelectProject(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@RequestParam(name = "newSelectedProject") long newSelectedProjectId,
			@ModelAttribute("selectedProject") Project selectedProject) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		if (newSelectedProjectId == -1) {
			selectedProject = null;
		} else {
			selectedProject = genericService.getByPK(Project.class, newSelectedProjectId);
		}

		addProjectRelatedAttributesToModel(model, selectedProject);
		return "/admin/Projects";
	}

	@GetMapping(path = { "admin/Projects.addTeamMember" })
	public String onActionFromAddTeamMember(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject, @RequestParam("inTeam") long inTeamId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);

		TeamMember newTm = new TeamMember();
		newTm.setTeam(genericService.getByPK(Team.class, inTeamId));
		model.addAttribute("newTm", newTm);
		model.addAttribute("listPersons", getPersons());
		return "/admin/Projects";
	}

	@GetMapping(path = { "admin/Projects.newTeam" })
	public String onActionFromNewTeam(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject,
			@RequestParam(name = "inProject") long inProjectId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		Team newTeam = new Team();
		model.addAttribute("newTeam", newTeam);
		model.addAttribute("inProject", inProjectId);
		return "/admin/Projects";
	}

	@GetMapping(path = { "admin/Projects.newDatabase" })
	public String onActionFromNewDatabase(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject,
			@RequestParam(name = "inProject") long inProjectId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		Database newDb = new Database();
		model.addAttribute("newDb", newDb);
		model.addAttribute("inProject", inProjectId);
		return "/admin/Projects";
	}

	@GetMapping(path = { "admin/Projects.newRepository" })
	public String onActionFromNewRepository(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject,
			@RequestParam(name = "inProject") long inProjectId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		Repository newRp = new Repository();
		model.addAttribute("newRp", newRp);
		model.addAttribute("inProject", inProjectId);
		return "/admin/Projects";
	}

	@PostMapping(path = { "admin/Projects.onSuccessFromNewTeamForm" })
	@Transactional
	public String onSuccessFromNewTeamForm(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject, @ModelAttribute("newTeam") Team newTeam,
			@ModelAttribute("inProject") long inProjectId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		Team teamToSave = new Team();
		teamToSave.setName(newTeam.getName());
		Responsibility teamResponsibility = new Responsibility();
		teamResponsibility.setTeam(teamToSave);
		teamResponsibility.setProject(genericService.getByPK(Project.class, inProjectId));
		genericService.save(teamToSave);
		genericService.save(teamResponsibility);
		return "redirect:/admin/Projects";
	}

	@GetMapping(path = { "admin/Projects.deleteTeamMember" })
	@Transactional
	public String deleteTeamMember(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject,
			@RequestParam("teamMemberId") long teamMemberId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		genericService.deleteByPK(TeamMember.class, teamMemberId);
		return "redirect:/admin/Projects";
	}

	@PostMapping(path = { "admin/Projects.onSuccessFromNewTeamMemberForm" })
	@Transactional
	public String onSuccessFromNewTeamMemberForm(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject, @ModelAttribute("newTm") TeamMember newTm,
			@RequestParam(name = "newSelectedPerson") long newSelectedPersonId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		newTm.setPerson(genericService.getByPK(Person.class, newSelectedPersonId));
		genericService.save(newTm);
		return "redirect:/admin/Projects";
	}

	@PostMapping(path = { "admin/Projects.onSuccessFromNewDatabaseForm" })
	@Transactional
	public String onSuccessFromNewDatabaseForm(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject, @ModelAttribute("newDb") Database newDb,
			@RequestParam("inProject") Long inProjectId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		newDb.setProject(genericService.getByPK(Project.class, inProjectId));
		genericService.save(newDb);
		return "redirect:/admin/Projects";
	}

	@PostMapping(path = { "admin/Projects.onSuccessFromNewRepositoryForm" })
	@Transactional
	public String onSuccessFromNewRepositoryForm(Model model, @ModelAttribute("userInfo") UserInfo userInfo,
			@ModelAttribute("selectedProject") Project selectedProject, @ModelAttribute("newRp") Repository newRp,
			@RequestParam("inProject") Long inProjectId) {
		model = modelOps.addMainModelAttribs("EPM - Admin - Projects", model, userInfo);
		addProjectRelatedAttributesToModel(model, selectedProject);
		newRp.setProject(genericService.getByPK(Project.class, inProjectId));
		genericService.save(newRp);

		return "redirect:/admin/Projects";
	}

	public List<Person> getPersons() {
		return (List<Person>) genericService.getAll(Person.class);
	}

	public List<Project> getAllProjects() {
		return (List<Project>) genericService.getAll(Project.class);
	}

	public List<Project> getProjects(Project selectedProject) {
		if (selectedProject != null) {
			List<Project> ls = new ArrayList<Project>();
			ls.add(genericService.getByPK(Project.class, selectedProject.getProjectId()));
			return ls;
		} else {
			return getAllProjects();
		}
	}

}
