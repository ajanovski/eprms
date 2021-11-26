package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.CourseProject;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class ProjectAutomation {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Property
	private Project project;

	@Property
	private Responsibility responsibility;

	@Property
	private TeamMember member;

	public List<Project> getProjects() {
		return ((List<Project>) genericService.getAll(Project.class)).stream()
				.filter(p -> p.getStatus() != null && p.getStatus().equals(ModelConstants.ProjectStatusCreation))
				.collect(Collectors.toList());
	}

	@CommitAfter
	public void onActionFromActivateAllListed() {
		for (Project p : getProjects()) {
			p.setStatus(ModelConstants.ProjectStatusActive);
		}
	}

	public String getCourseCode() {
		List<CourseProject> list = responsibility.getProject().getCourseProjects();
		if (list != null && list.size() > 0) {
			return (list.get(0).getCourse().getCode());
		} else {
			return ModelConstants.CourseUnknown;
		}
	}

}
