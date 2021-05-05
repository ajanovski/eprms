package info.ajanovski.eprms.tap.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
public class MyProjectReports {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private ProjectManager projectManager;

	public List<Project> getMyProjects() {
		return projectManager.getProjectByPerson(userInfo.getPersonId());
	}

	@Property
	private Project project;

	@Property
	private Activity activity;

	@Property
	private WorkReport workReport;

	@Property
	private WorkEvaluation workEvaluation;

}
