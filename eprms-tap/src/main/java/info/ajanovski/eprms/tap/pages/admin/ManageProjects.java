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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.SelectModelFactory;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.CourseProject;
import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.Team;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.model.util.ActivityComparatorViaType;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.mq.MessagingService;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.CourseManager;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.services.TranslationService;
import info.ajanovski.eprms.tap.util.AppConfig;
import info.ajanovski.eprms.tap.util.AppConstants;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class ManageProjects {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private Logger logger;

	@Inject
	private ProjectManager projectManager;

	@Inject
	private CourseManager courseManager;

	@Inject
	private SystemConfigService systemConfigService;

	@Inject
	private MessagingService messagingService;

	@Inject
	private GenericService genericService;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private TranslationService translationService;

	@Inject
	private PersistentLocale persistentLocale;

	@Inject
	private PersonManager personManager;

	@Inject
	private Messages messages;

	@Property
	private Project project;

	@Persist
	@Property
	private Project selectedProject;

	@Persist
	@Property
	private String selectedStatus;

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
	private Responsibility linkResponsibility;

	@Persist
	@Property
	private Database newDb;

	@Persist
	@Property
	private Repository newRp;

	@Persist
	@Property
	private List<Course> inCourses;

	@Persist
	@Property
	private Project copyActivitiesFromProject;

	@Persist
	@Property
	private Course selectedCourse;

	@Property
	private String personSearch;

	void onActivate() {
	}

	public List<Project> getAllProjects() {
		List<Project> list = (List<Project>) projectManager.getAllProjects();
		if (selectedCourse == null) {
			return new ArrayList<Project>();
		} else {
			if (selectedStatus == null) {
				return list.stream()
						.filter(p -> (p.getCourseProjects().stream()
								.anyMatch(cp -> cp.getCourse().getCourseId() == selectedCourse.getCourseId())))
						.collect(Collectors.toList());
			} else {
				return list.stream()
						.filter(p -> (p.getCourseProjects().stream()
								.anyMatch(cp -> cp.getCourse().getCourseId() == selectedCourse.getCourseId()
										&& selectedStatus.equals(cp.getProject().getStatus()))))
						.collect(Collectors.toList());
			}
		}
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

	public void onActionFromAddTeamMember(Team t) {
		newTm = new TeamMember();
		newTm.setTeam(t);
	}

	public void onActionFromNewProject() {
		newProject = new Project();
		inCourses = new ArrayList<Course>();
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

	public void onActionFromLinkTeam(Project p) {
		linkResponsibility = new Responsibility();
		linkResponsibility.setProject(p);
	}

	public void onActionFromNewDatabase(Project p) {
		newDb = new Database();
		newDb.setProject(p);
		String dbPrefix = systemConfigService.getString(AppConstants.SystemParameterDBCreationPrefix);
		String tunnelPrefix = systemConfigService.getString(AppConstants.SystemParameterDBTunnelPrefix);
		String ownerSuffix = systemConfigService.getString(AppConstants.SystemParameterDBCreationOwnerSuffix);
		String prjcode = p.getCode().toLowerCase().replace("-", "_").replace(" ", "_");
		newDb.setType(systemConfigService.getString(AppConstants.SystemParameterDBServerType));
		newDb.setServer(systemConfigService.getString(AppConstants.SystemParameterDBServerName));
		newDb.setPort(systemConfigService.getString(AppConstants.SystemParameterDBServerPort));
		String dbName = (dbPrefix + prjcode).toLowerCase().replace("-", "_").replace(" ", "_");
		newDb.setName(dbName);
		newDb.setOwner(dbName + ownerSuffix);
		newDb.setPassword(generateRandomHexToken(6));
		newDb.setTunnelServer(systemConfigService.getString(AppConstants.SystemParameterDBTunnelServerName));
		newDb.setTunnelUser(tunnelPrefix + prjcode);
		newDb.setTunnelPassword(generateRandomHexToken(4));
	}

	public static String generateRandomHexToken(int byteLength) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] token = new byte[byteLength];
		secureRandom.nextBytes(token);
		return new BigInteger(1, token).toString(16);
	}

	public void onActionFromNewRepository(Project p) {
		newRp = new Repository();
		newRp.setProject(p);
	}

	public List<Course> getAllCourses() {
		return courseManager.getAllCoursesByPerson(userInfo.getPersonId());
	}

	public List<Team> getAllTeams() {
		return (List<Team>) genericService.getAll(Team.class);
	}

	public SelectModel getCoursesModel() {
		return selectModelFactory.create(getAllCourses(), "title");
	}

	public SelectModel getAllTeamsModel() {
		return selectModelFactory.create(getAllTeams(), "name");
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

	private Boolean cancelTeamMemberForm=false;
	
	public void onCanceledFromTeamMemberForm() {
		cancelTeamMemberForm=true;
	}

	@CommitAfter
	public void onSuccessFromTeamMemberForm() {
		if (!cancelTeamMemberForm) {
			if (personSearch != null && personSearch.length() > 0) {
				Pattern pattern = Pattern.compile("\\[(.*?)\\]");
				Matcher matcher = pattern.matcher(personSearch);
				if (matcher.find()) {
					newTm.setPerson(personManager.getPersonByUsername(matcher.group(1)));
				}
			}
			genericService.save(newTm);
		}
		newTm = null;
	}

	@CommitAfter
	public void onSuccessFromNewProjectForm() {
		genericService.saveOrUpdate(newProject);
		projectManager.addCoursesToProject(inCourses, newProject);
		selectedProject = newProject;
		newProject = null;
	}

	public void onCancelNewProject() {
		newProject = null;
	}

	@CommitAfter
	public void onSuccessFromNewTeamForm() {
		genericService.saveOrUpdate(newTeam);
		if (newResponsibility != null) {
			genericService.saveOrUpdate(newResponsibility.getTeam());
			genericService.saveOrUpdate(newResponsibility);
		}
		newTeam = null;
		newResponsibility = null;
	}

	@CommitAfter
	public void onSuccessFromLinkTeamForm() {
		if (linkResponsibility != null) {
			genericService.saveOrUpdate(linkResponsibility);
		}
		linkResponsibility = null;
	}

	@CommitAfter
	public void onSuccessFromNewDatabaseForm() {
		genericService.save(newDb);
		try {
			messagingService.setupMQHost(AppConfig.getString("MQHost"));
			messagingService.sendDatabaseNotification(newDb);
		} catch (Exception e) {
			logger.error("DB creation message not sent");
		}
		newDb = null;
	}

	@CommitAfter
	public void onSuccessFromNewRepositoryForm() {
		genericService.save(newRp);
		try {
			messagingService.setupMQHost(AppConfig.getString("MQHost"));
			messagingService.sendRepositoryNotification(newRp);
		} catch (Exception e) {
			logger.error("REPO creation message not sent");
		}
		newRp = null;
	}

	@Property
	private Person person;

	public List<Person> getPersons() {
		return ((List<Person>) genericService.getAll(Person.class)).stream()
				.sorted((o1, o2) -> o1.getUserName().compareTo(o2.getUserName())).toList();
	}

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

	public void onActionFromCopyActivities(Project p) {
		copyActivitiesFromProject = p;
	}

	public void onActionFromResetClipboard() {
		copyActivitiesFromProject = null;
	}

	@CommitAfter
	public void onActionFromPasteActivities(Project pasteActivitiesToProject) {
		List<Activity> activities = genericService.getByPK(Project.class, copyActivitiesFromProject.getProjectId())
				.getActivities();
		Collections.sort(activities, new ActivityComparatorViaType());
		for (Activity original : activities) {
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

	public String getCourseTitle() {
		String trans = translationService.getTranslation(Course.class.getSimpleName(), "title", course.getCourseId(),
				persistentLocale.get().getLanguage());
		if (trans == null) {
			return course.getTitle();
		} else {
			return trans;
		}
	}

	@CommitAfter
	void onActionFromDeleteResponsibilityAndTeam(Responsibility r) {
		Team t = r.getTeam();
		genericService.delete(r);
		if (t.getResponsibilities().size() == 0) {
			genericService.delete(t);
		}
	}

	@CommitAfter
	void onActionFromDeleteResponsibility(Responsibility r) {
		genericService.delete(r);
	}

	void onActionFromEditTeam(Team t) {
		newTeam = t;
	}

	void onCancelNewTeam() {
		newTeam = null;
	}

	void onCancelLinkTeam() {
		linkResponsibility = null;
	}

	@CommitAfter
	void onActionFromChangeStatus(Project p) {
		projectManager.cycleStatus(p);
	}

	public String[] getModelProjectStatuses() {
		return ModelConstants.AllProjectStatuses;
	}

	@CommitAfter
	public void onActionFromDeleteProject(Project p) {
		try {
			for (CourseProject cp : p.getCourseProjects()) {
				genericService.delete(cp);
			}
			genericService.delete(p);
		} catch (Exception e) {

		}
	}

	public List<String> onProvideCompletionsFromSelectPerson(String input) {
		List<String> list = new ArrayList<String>();
		if (input != null && input.length() > 0) {
			getPersons().stream()
					.filter(p -> (p.getFirstName() + p.getLastName() + p.getEmail() + p.getUserName()).contains(input))
					.forEach(p -> list.add(p.getFirstName() + " " + p.getLastName() + " [" + p.getUserName() + "]"));
		}
		return list;
	}

}
