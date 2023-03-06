package info.ajanovski.eprms.tap.pages.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.http.services.Request;
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
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
@Import(module = { "bootstrap/modal", "bootstrap/collapse", "zoneUpdateEffect" })
public class MyProjects {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private Logger logger;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private SelectModelFactory selectModelFactory;

	@InjectComponent
	private Zone zNPModal;

	@InjectComponent
	private Zone zNTModal;

	@InjectComponent
	private Zone zJNTModal;

	@InjectComponent
	private Zone zJTModal;

	@Inject
	private ProjectManager projectManager;

	@Inject
	private GenericService genericService;

	public List<Project> getMyProjects() {
		return projectManager.getProjectByPerson(userInfo.getPersonId());
	}

	@Property
	private Project project;

	@Property
	private Activity activity;

	@Property
	@Persist
	private Course selectedCourse;

	private Boolean cancelForm = false;

	@Property
	@Persist
	private Project newProject;

	@Property
	@Persist
	private Team teamToEdit;

	@Property
	@Persist
	private Boolean teamNew;

	@Property
	private TeamMember myTeamMember;

	@Property
	private TeamMember teamMember;

	@Property
	@Persist
	private Boolean chooseATeam;

	@Persist
	@Property
	private Team selectedTeam;

	@Property
	@Persist
	private Team addToTeam;

	@Property
	@Persist
	private String newMember;

	public void setupRender() {
		if (teamNew == null) {
			if (teamToEdit != null) {
				teamToEdit = genericService.getByPK(Team.class, teamToEdit.getTeamId());
			}
		}
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

	public void onActionFromProposeProject() {
		newProject = new Project();
		newProject.setStatus(ModelConstants.ProjectStatusProposed);
		newProject.setStartDate(new Date());
	}

	public void onActionFromEditProject(Project p) {
		if (p.getResponsibilities() != null && p.getResponsibilities().size() > 0) {
			selectedTeam = p.getResponsibilities().get(0).getTeam();
		}
		if (p.getCourseProjects() != null && p.getCourseProjects().size() > 0) {
			selectedCourse = p.getCourseProjects().get(0).getCourse();
		}
		newProject = p;
	}

	void onCancelNewProject() {
		newProject = null;
		selectedTeam = null;
		selectedCourse = null;
	}

	public void onSuccessFromFrmProposeProject() {
		newProject = null;
		selectedTeam = null;
		selectedCourse = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNPModal);
		}
	}

	@CommitAfter
	public void onValidateFromFrmProposeProject() {
		genericService.saveOrUpdate(newProject);
		if (selectedTeam != null) {
			if (!(newProject.getResponsibilities().stream()
					.anyMatch(p -> p.getTeam().getTeamId() == selectedTeam.getTeamId()))) {
				Responsibility r = new Responsibility();
				r.setProject(newProject);
				r.setTeam(selectedTeam);
				List<Responsibility> listr = new ArrayList<Responsibility>();
				listr.add(r);
				newProject.setResponsibilities(listr);
				genericService.saveOrUpdate(r);
			}
		}
		if (selectedCourse != null) {
			if (!(newProject.getCourseProjects().stream()
					.anyMatch(p -> p.getCourse().getCourseId() == selectedCourse.getCourseId()))) {
				CourseProject cp = new CourseProject();
				cp.setProject(newProject);
				cp.setCourse(selectedCourse);
				List<CourseProject> listcp = new ArrayList<CourseProject>();
				listcp.add(cp);
				newProject.setCourseProjects(listcp);
				genericService.saveOrUpdate(cp);
			}
		}
		genericService.saveOrUpdate(newProject);
	}

	public void onActionFromProposeTeam() {
		teamNew = true;
		teamToEdit = new Team();
		teamToEdit.setCreatedDate(new Date());
		teamToEdit.setStatusDate(new Date());
		teamToEdit.setStatus(ModelConstants.TeamStatusProposed);
	}

	void onCancelNewTeam() {
		teamToEdit = null;
	}

	public void onSuccessFromFrmProposeTeam() {
		teamToEdit = null;
		teamNew = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNTModal);
		}
	}

	@CommitAfter
	public void onValidateFromFrmProposeTeam() {
		genericService.saveOrUpdate(teamToEdit);
		if (!(teamToEdit.getTeamMembers().stream()
				.anyMatch(p -> p.getPerson().getPersonId() == getMyself().getPersonId()))) {
			TeamMember tm = new TeamMember();
			tm.setPerson(getMyself());
			tm.setPositionNumber(1);
			tm.setRole(ModelConstants.TeamMemberRoleCoordinator);
			tm.setTeam(teamToEdit);
			tm.setStatus(ModelConstants.TeamMemberStatusAccepted);
			tm.setCreatedDate(new Date());
			tm.setStatusDate(new Date());
			genericService.saveOrUpdate(tm);
		}
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

	void onActionFromChooseTeamToJoin() {
		chooseATeam = true;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJTModal);
		}
	}

	public SelectModel getJoinableTeams() {
		List<Team> lista = (List<Team>) genericService.getAll(Team.class);
		return selectModelFactory.create(lista.stream()
				.filter(p -> p.getOpenForNewMembers() != null && p.getOpenForNewMembers() == true
						&& !(getMyMemberTeams().stream().anyMatch(q -> q.getTeam().getTeamId() == p.getTeamId())))
				.collect(Collectors.toList()), "name");
	}

	@CommitAfter
	public void onValidateFromFrmJoinTeam() {
		if (selectedTeam != null) {
			TeamMember tm = new TeamMember();
			tm.setPerson(getMyself());
			tm.setRole(ModelConstants.TeamMemberRoleMember);
			tm.setTeam(selectedTeam);
			tm.setStatus(ModelConstants.TeamMemberStatusProposed);
			tm.setCreatedDate(new Date());
			tm.setStatusDate(new Date());
			genericService.saveOrUpdate(tm);
		}
	}

	public void onSuccessFromFrmJoinTeam() {
		selectedTeam = null;
		chooseATeam = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJTModal);
		}
	}

	public void onCancelJoinTeam() {
		selectedTeam = null;
		chooseATeam = null;
	}

	public void onActionFromEditTeam(TeamMember tm) {
		teamToEdit = tm.getTeam();
	}

	public boolean isCanApprove() {
		if (myTeamMember.getTeam().getTeamMembers().stream()
				.anyMatch(p -> p.getPerson().getPersonId() == getMyself().getPersonId() && p.getRole() != null
						&& p.getRole().equals(ModelConstants.TeamMemberRoleCoordinator))) {
			if (teamMember != null && !(teamMember.getStatus() != null
					&& teamMember.getStatus().equals(ModelConstants.TeamMemberStatusAccepted))) {
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

	public void onActionFromAddMembers(TeamMember tm) {
		newMember = new String("brindeks");
		addToTeam = tm.getTeam();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJNTModal);
		}
	}

	public void onCancelAddMembers() {
		newMember = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJNTModal);
		}
	}

	@Inject
	private PersonManager personManager;

	@CommitAfter
	public void onValidateFromFrmAddMembers() {
		if (newMember != null) {
			Person p = personManager.getPersonByUsername(newMember);
			if (p != null) {
				TeamMember tm = new TeamMember();
				tm.setPerson(p);
				tm.setRole(ModelConstants.TeamMemberRoleMember);
				tm.setTeam(addToTeam);
				tm.setStatus(ModelConstants.TeamMemberStatusProposed);
				tm.setCreatedDate(new Date());
				tm.setStatusDate(new Date());
				genericService.saveOrUpdate(tm);
			}
		}
	}

	public void onSuccessFromFrmAddMembers() {
		newMember = null;
		addToTeam = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zJNTModal);
		}
	}

	@CommitAfter
	public void onActionFromApproveTeamMember(TeamMember tm) {
		tm.setStatus(ModelConstants.TeamMemberStatusAccepted);
	}

	public boolean isProjectEditable() {
		if (project != null) {
			if (project.getStatus().equals(ModelConstants.ProjectStatusProposed)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
