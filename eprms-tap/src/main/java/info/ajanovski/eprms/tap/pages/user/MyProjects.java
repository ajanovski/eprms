package info.ajanovski.eprms.tap.pages.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.CourseProject;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.Team;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.model.util.CourseComparator;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
@Import(module = { "bootstrap/modal", "bootstrap/collapse", "zoneUpdateEffect" })
public class MyProjects {
	@Inject
	private Logger logger;
	@Inject
	private Request request;
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	@Inject
	private SelectModelFactory selectModelFactory;
	@Inject
	private SystemConfigService systemConfigService;

	@Inject
	private ProjectManager projectManager;
	@Inject
	private GenericService genericService;
	@Inject
	private PersonManager personManager;

	@InjectComponent
	private Zone zNPModal;
	@InjectComponent
	private Zone zNTModal;
	@InjectComponent
	private Zone zJNTModal;
	@InjectComponent
	private Zone zJTModal;

	@SessionState
	@Property
	private UserInfo userInfo;

	@InjectComponent
	private Form frmCreateTeam;
	@InjectComponent
	private Form frmUpdateTeam;
	@InjectComponent
	private Form frmCreateProject;
	@InjectComponent
	private Form frmUpdateProject;
	@InjectComponent
	private Form frmAddTeamMember;

	public enum Mode {
		CREATETEAM, UPDATETEAM, CREATEPROJECT, UPDATEPROJECT, ADDTEAMMEMBER, CHOOSETEAMTOJOIN
	}

	@Property
	private Course selectedCourse;

	@Property
	private Team selectedTeam;

	@Property
	private Mode editorMode;

	@Property
	private Long editorTeamId;

	@Property
	private Long editorProjectId;

	@Property
	private Long editorTeamIdToAddMember;

	@Property
	private Team editorTeam;

	@Property
	private Project editorProject;

	@Property
	private String personSearch;

	@Property
	private Project project;

	@Property
	private Activity activity;

	@Property
	private TeamMember myTeamMember;

	@Property
	private TeamMember teamMember;

	@Property
	private Team joinableTeam;

	@Property
	private CourseProject listedCourseProject;

	@Property
	private Responsibility joinableTeamResponsbility;

	@Property
	private CourseProject joinableTeamResponsbilityCourseProject;

	// Activate, Passivate

	void onActivate(EventContext ec) {
		if (ec.getCount() == 0) {
			editorMode = null;
			editorTeamId = null;
			editorProjectId = null;
			editorTeamIdToAddMember = null;
		} else if (ec.getCount() == 1) {
			editorMode = ec.get(Mode.class, 0);
			editorTeamId = null;
			editorProjectId = null;
			editorTeamIdToAddMember = null;
		} else {
			editorMode = ec.get(Mode.class, 0);
			if (editorMode == Mode.CREATETEAM) {
				editorTeamId = ec.get(Long.class, 1);
			} else if (editorMode == Mode.UPDATETEAM) {
				editorTeamId = ec.get(Long.class, 1);
			} else if (editorMode == Mode.CREATEPROJECT) {
				editorProjectId = ec.get(Long.class, 1);
			} else if (editorMode == Mode.UPDATEPROJECT) {
				editorProjectId = ec.get(Long.class, 1);
			} else if (editorMode == Mode.ADDTEAMMEMBER) {
				editorTeamIdToAddMember = ec.get(Long.class, 1);
			}
		}
	}

	Object[] onPassivate() {
		if (editorMode == null) {
			return null;
		} else if (editorMode == Mode.CREATETEAM) {
			return new Object[] { editorMode };
		} else if (editorMode == Mode.UPDATETEAM) {
			return new Object[] { editorMode, editorTeamId };
		} else if (editorMode == Mode.CREATEPROJECT) {
			return new Object[] { editorMode };
		} else if (editorMode == Mode.UPDATEPROJECT) {
			return new Object[] { editorMode, editorProjectId };
		} else if (editorMode == Mode.ADDTEAMMEMBER) {
			return new Object[] { editorMode, editorTeamIdToAddMember };
		} else if (editorMode == Mode.CHOOSETEAMTOJOIN) {
			return new Object[] { editorMode };
		} else {
			throw new IllegalStateException(editorMode.toString());
		}
	}

	// Create Team

	public boolean isModeCreateTeam() {
		return editorMode == Mode.CREATETEAM;
	}

	void onActionFromNewTeam() {
		editorMode = Mode.CREATETEAM;
		editorTeamId = null;
	}

	void onCancelNewTeam() {
		editorMode = null;
		editorTeamId = null;
	}

	void onPrepareForRenderFromFrmCreateTeam() throws Exception {
		if (frmCreateTeam.isValid()) {
			editorMode = Mode.CREATETEAM;
			editorTeam = new Team();
		}
	}

	void onPrepareForSubmitFromFrmCreateTeam() throws Exception {
		editorMode = Mode.CREATETEAM;
		editorTeam = new Team();
	}

	@CommitAfter
	void onValidateFromFrmCreateTeam() {
		if (frmCreateTeam.getHasErrors()) {
			return;
		}
		try {
			Date d = new Date();
			editorTeam.setCreatedDate(d);
			editorTeam.setStatus(ModelConstants.TeamStatusProposed);
			genericService.saveOrUpdate(editorTeam);
			TeamMember tm = new TeamMember();
			tm.setPerson(getMyself());
			tm.setTeam(editorTeam);
			tm.setCreatedDate(d);
			tm.setStatus(ModelConstants.TeamMemberStatusAccepted);
			tm.setRole(ModelConstants.TeamMemberRoleCoordinator);
			genericService.saveOrUpdate(tm);
		} catch (Exception e) {
			frmCreateTeam.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmCreateTeam() {
		editorMode = null;
		editorTeamId = editorTeam.getTeamId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNTModal);
		}
	}

	// Update Team

	public boolean isModeUpdateTeam() {
		return editorMode == Mode.UPDATETEAM;
	}

	void onActionFromEditTeam(Team t) {
		editorMode = Mode.UPDATETEAM;
		editorTeamId = t.getTeamId();
	}

	void onCancelUpdateTeam() {
		editorMode = null;
		editorTeamId = null;
	}

	void onPrepareForRenderFromFrmUpdateTeam() {
		if (frmUpdateTeam.isValid()) {
			editorMode = Mode.UPDATETEAM;
			editorTeam = genericService.getByPK(Team.class, editorTeamId);
		}
	}

	void onPrepareForSubmitFromFrmUpdateTeam() {
		editorMode = Mode.UPDATETEAM;
		editorTeam = genericService.getByPK(Team.class, editorTeamId);
		if (editorTeam == null) {
			frmUpdateTeam.recordError("Param has been deleted by another process.");
			editorTeam = new Team();
		}
	}

	@CommitAfter
	void onValidateFromFrmUpdateTeam() {
		if (frmUpdateTeam.getHasErrors()) {
			return;
		}
		try {
			genericService.saveOrUpdate(editorTeam);
		} catch (Exception e) {
			frmUpdateTeam.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmUpdateTeam() {
		editorMode = null;
		editorTeamId = editorTeam.getTeamId();
	}

	////////////////////////////// End Team

	// Choose a Team to Join

	public boolean isModeChooseTeamToJoin() {
		return editorMode == Mode.CHOOSETEAMTOJOIN;
	}

	void onActionFromChooseTeamToJoin() {
		editorMode = Mode.CHOOSETEAMTOJOIN;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJTModal);
		}
	}

	// Create Project

	public boolean isModeCreateProject() {
		return editorMode == Mode.CREATEPROJECT;
	}

	void onActionFromNewProject() {
		editorMode = Mode.CREATEPROJECT;
		editorProjectId = null;
	}

	void onCancelNewProject() {
		editorMode = null;
		editorProjectId = null;
	}

	void onPrepareForRenderFromFrmCreateProject() throws Exception {
		if (frmCreateProject.isValid()) {
			editorMode = Mode.CREATEPROJECT;
			editorProject = new Project();
		}
	}

	void onPrepareForSubmitFromFrmCreateProject() throws Exception {
		editorMode = Mode.CREATEPROJECT;
		editorProject = new Project();
	}

	@CommitAfter
	void onValidateFromFrmCreateProject() {
		if (frmCreateProject.getHasErrors()) {
			return;
		}
		try {
			Date d = new Date();
			editorProject.setStartDate(d);
			editorProject.setStatus(ModelConstants.ProjectStatusProposed);
			genericService.saveOrUpdate(editorProject);
			Responsibility r = new Responsibility();
			r.setProject(editorProject);
			r.setTeam(selectedTeam);
			genericService.saveOrUpdate(r);
			CourseProject cp = new CourseProject();
			cp.setProject(editorProject);
			cp.setCourse(selectedCourse);
			genericService.saveOrUpdate(cp);
		} catch (Exception e) {
			frmCreateProject.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmCreateProject() {
		editorMode = null;
		editorProjectId = editorProject.getProjectId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNTModal);
		}
	}

	// Update Project

	public boolean isModeUpdateProject() {
		return editorMode == Mode.UPDATEPROJECT;
	}

	void onActionFromEditProject(Project p) {
		editorMode = Mode.UPDATEPROJECT;
		editorProjectId = p.getProjectId();
	}

	void onCancelUpdateProject() {
		editorMode = null;
		editorTeamId = null;
	}

	void onPrepareForRenderFromFrmUpdateProject() {
		if (frmUpdateProject.isValid()) {
			editorMode = Mode.UPDATEPROJECT;
			editorProject = genericService.getByPK(Project.class, editorProjectId);

			if (editorProject.getResponsibilities() != null && editorProject.getResponsibilities().size() > 0) {
				selectedTeam = editorProject.getResponsibilities().get(0).getTeam();
			}
			if (editorProject.getCourseProjects() != null && editorProject.getCourseProjects().size() > 0) {
				selectedCourse = editorProject.getCourseProjects().get(0).getCourse();
			}
		}
	}

	void onPrepareForSubmitFromFrmUpdateProject() {
		editorMode = Mode.UPDATEPROJECT;
		editorProject = genericService.getByPK(Project.class, editorProjectId);
		if (editorProject == null) {
			frmUpdateProject.recordError("Param has been deleted by another process.");
			editorProject = new Project();
		}
	}

	@CommitAfter
	void onValidateFromFrmUpdateProject() {
		if (frmUpdateProject.getHasErrors()) {
			return;
		}
		try {
			genericService.saveOrUpdate(editorProject);
		} catch (Exception e) {
			frmUpdateProject.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmUpdateProject() {
		editorMode = null;
		editorProjectId = editorProject.getProjectId();
	}

	////////////////////////////// End Project

	// Add Team Member

	public boolean isModeAddTeamMember() {
		return editorMode == Mode.ADDTEAMMEMBER;
	}

	void onActionFromAddTeamMember(Team t) {
		editorMode = Mode.ADDTEAMMEMBER;
		editorTeamIdToAddMember = t.getTeamId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJNTModal);
		}
	}

	void onCancelAddTeamMember() {
		editorMode = null;
		editorTeamIdToAddMember = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJNTModal);
		}
	}

	void onPrepareForRenderFromFrmAddTeamMember() throws Exception {
		if (frmCreateProject.isValid()) {
			editorMode = Mode.ADDTEAMMEMBER;
		}
	}

	void onPrepareForSubmitFromFrmAddTeamMember() throws Exception {
		editorMode = Mode.ADDTEAMMEMBER;
	}

	@CommitAfter
	void onValidateFromFrmAddTeamMember() {
		if (frmAddTeamMember.getHasErrors()) {
			return;
		}
		try {
			if (personSearch != null) {
				Person p = null;
				if (personSearch != null && personSearch.length() > 3) {
					Pattern pattern = Pattern.compile("\\[(.*?)\\]");
					Matcher matcher = pattern.matcher(personSearch);
					if (matcher.find()) {
						p = personManager.getPersonByUsername(matcher.group(1));
					}
				}

				if (p != null) {
					TeamMember tm = new TeamMember();
					tm.setPerson(p);
					tm.setRole(ModelConstants.TeamMemberRoleMember);
					tm.setTeam(genericService.getByPK(Team.class, editorTeamIdToAddMember));
					tm.setStatus(ModelConstants.TeamMemberStatusProposed);
					tm.setCreatedDate(new Date());
					tm.setStatusDate(new Date());
					genericService.saveOrUpdate(tm);
				}
			}
		} catch (Exception e) {
			frmCreateProject.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmAddTeamMember() {
		personSearch = null;
		editorMode = null;
		editorTeamIdToAddMember = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJNTModal);
		}
	}

	public List<String> onProvideCompletionsFromSelectPerson(String input) {
		List<String> list = new ArrayList<String>();
		if (input != null && input.length() >= 3) {
			personManager.getPersonByFilter(input).stream()
					.filter(p -> (p.getFirstName() + p.getLastName() + p.getEmail() + p.getUserName()).contains(input))
					.forEach(p -> list.add(p.getFirstName() + " " + p.getLastName() + " [" + p.getUserName() + "]"));
		}
		return list;
	}

	//// FINISH Add Team Member

	public List<Project> getMyProjects() {
		return projectManager.getProjectByPerson(userInfo.getPersonId());
	}

	public SelectModel getAllCourses() {
		CourseComparator cc = new CourseComparator();
		List<Course> lista = (List<Course>) genericService.getAll(Course.class);
		Collections.sort(lista, cc);
		return selectModelFactory.create(lista, "title");
	}

	public List<TeamMember> getMyMemberTeams() {
		return projectManager.getTeamMembershipOfPerson(userInfo.getPersonId());
	}

	public List<Team> getMyTeams() {
		return projectManager.getTeamMembershipOfPerson(userInfo.getPersonId()).stream().map(TeamMember::getTeam)
				.toList();
	}

	public SelectModel getMyTeamsModel() {
		List<Team> lista = getMyTeams();
		return selectModelFactory.create(lista, "name");
	}

	public Person getMyself() {
		return genericService.getByPK(Person.class, userInfo.getPersonId());
	}

	@CommitAfter
	void onActionFromLeaveTeam(TeamMember tm) {
		Team t = tm.getTeam();
		t.getTeamMembers().remove(tm);
		genericService.delete(tm);
		if (t.getTeamMembers().size() == 0) {
			for (Responsibility r : t.getResponsibilities()) {
				Project p = r.getProject();
				p.getResponsibilities().remove(r);
				genericService.delete(r);
				if (p.getResponsibilities().size() == 0) {
					for (CourseProject cp : p.getCourseProjects()) {
						genericService.delete(cp);
					}
					genericService.delete(p);
				}
			}
			genericService.deleteByPK(Team.class, t.getTeamId());
		}
	}

	public List<Team> getListOfJoinableTeams() {
		List<Team> lista = (List<Team>) genericService.getAll(Team.class);
		return lista.stream()
				.filter(p -> p.getOpenForNewMembers() != null && p.getOpenForNewMembers() == true
						&& p.getStatus().equals(ModelConstants.TeamStatusProposed)
						&& !(getMyMemberTeams().stream().anyMatch(q -> q.getTeam().getTeamId() == p.getTeamId())))
				.collect(Collectors.toList());
	}

	public SelectModel getJoinableTeams() {
		return selectModelFactory.create(getListOfJoinableTeams(), "name");
	}

	@CommitAfter
	public void onActionFromJoinTeam(Team t) {
		TeamMember tm = new TeamMember();
		tm.setPerson(getMyself());
		tm.setRole(ModelConstants.TeamMemberRoleMember);
		tm.setTeam(t);
		tm.setStatus(ModelConstants.TeamMemberStatusProposed);
		tm.setCreatedDate(new Date());
		tm.setStatusDate(new Date());
		genericService.saveOrUpdate(tm);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJTModal);
		}
	}

	public void onCancelJoinTeam() {
		editorMode = null;
	}

	public boolean isProjectCoordinator() {
		if (myTeamMember.getPerson().getPersonId() == getMyself().getPersonId()) {
			// This myTeamMember is the logged-in user
			if (myTeamMember.getRole() != null
					&& myTeamMember.getRole().equals(ModelConstants.TeamMemberRoleCoordinator)) {
				// this myTeamMember is a Team Coordinator
				return true;
			} else {
				// this myTeamMember is NOT a Team Coordinator
				return false;
			}
		} else {
			// this myteammember is not the logged-in user
			return false;
		}
	}

	public boolean isCanApprove() {
		if (isProjectCoordinator()) {
			if (myTeamMember.getTeam().getResponsibilities().stream()
					.allMatch(r -> r.getProject().getStatus().equals(ModelConstants.ProjectStatusProposed))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isCanApproveTeamMember() {
		if (isCanApprove()) {
			if (!teamMember.getStatus().equals(ModelConstants.TeamMemberStatusAccepted)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isCanLeave() {
		if (isProjectCoordinator()) {
			logger.debug("team {} isProjectCoordinator true", myTeamMember.getTeam().getName());
			return false;
		} else {
			logger.debug("team {} isProjectCoordinator false", myTeamMember.getTeam().getName());
			logger.debug(" team {} num resp {}", myTeamMember.getTeam().getName(),
					myTeamMember.getTeam().getResponsibilities().size());
			if (myTeamMember.getTeam().getResponsibilities().stream()
					.allMatch(r -> r.getProject().getStatus().equals(ModelConstants.ProjectStatusProposed))) {
				logger.debug(" team {} allprojects status proposed: true", myTeamMember.getTeam().getName());
				return true;
			} else {
				logger.debug(" team {} allprojects status proposed: false", myTeamMember.getTeam().getName());
				return false;
			}
		}
	}

	public boolean isCanRemoveTeam() {
		if (isProjectCoordinator()) {
			if (myTeamMember.getTeam().getTeamMembers().size() == 1 && myTeamMember.getTeam().getStatus() != null
					&& myTeamMember.getTeam().getStatus().equals(ModelConstants.TeamStatusProposed)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isCanRemoveMember() {
		if (isProjectCoordinator()) {
			if (teamMember != null
					&& !(teamMember.getStatus() != null
							&& teamMember.getStatus().equals(ModelConstants.TeamMemberStatusAccepted))
					&& teamMember.getPerson().getPersonId() != getMyself().getPersonId()) {
				return true;
			} else {
				if (teamMember == null) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	@CommitAfter
	public void onActionFromRemoveTeamMember(TeamMember tm) {
		genericService.delete(tm);
	}

	@CommitAfter
	public void onActionFromRemoveTeam(TeamMember tm) {
		Team t = tm.getTeam();
		genericService.delete(tm);
		genericService.delete(t);
	}

	@CommitAfter
	public void onActionFromApproveTeamMember(TeamMember tm) {
		tm.setStatus(ModelConstants.TeamMemberStatusAccepted);
	}

	public boolean isProjectEditable() {
		if (project != null) {
			if (project.getStatus() == null) {
				return true;
			} else if (project.getStatus().equals(ModelConstants.ProjectStatusProposed)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isCanRemoveProject() {
		if (project != null) {
			if ((project.getStatus() == null || project.getStatus().equals(ModelConstants.ProjectStatusProposed))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@CommitAfter
	void onActionFromRemoveProject(Project p) {
		p.getCourseProjects().forEach(cp -> genericService.delete(cp));
		p.getResponsibilities().forEach(r -> genericService.delete(r));
		genericService.delete(p);
	}

	public String getProjectURL() {
		return projectManager.getProjectURL(project);
	}

	public boolean isProjectUrlShown() {
		if (project.getStatus().equals(ModelConstants.ProjectStatusActive)) {
			return true;
		} else {
			return false;
		}
	}

}
