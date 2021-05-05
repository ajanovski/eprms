package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
public class OverallCourseReport {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private ProjectManager projectManager;

	@Inject
	private GenericService genericService;

	public List<Project> getAllProjects() {
		if (selectedCourse == null) {
			return (List<Project>) projectManager.getAllProjectsOrderByTitle();
		} else {
			return (List<Project>) projectManager.getCourseProjectsOrderByTitle(selectedCourse);
		}
	}

	@Property
	private Project project;

	@Property
	private Activity activity;
	@Property
	private WorkReport workReport;
	@Property
	private WorkEvaluation workEvaluation;

	@Persist
	@Property
	private WorkReport newWorkReport;

	public void onActionFromAddWorkReport(Activity a) {
		newWorkEvaluation = null;
		newWorkReport = new WorkReport();
		newWorkReport.setActivity(a);
	}

	@CommitAfter
	public void onSuccessFromFrmAddWorkReport() {
		genericService.saveOrUpdate(newWorkReport);
		newWorkReport = null;
		newWorkEvaluation = null;
	}

	@Persist
	@Property
	private WorkEvaluation newWorkEvaluation;

	public void onActionFromAddWorkEvaluation(WorkReport wr) {
		newWorkReport = null;
		newWorkEvaluation = new WorkEvaluation();
		newWorkEvaluation.setWorkReport(wr);
	}

	public void onActionFromEditWorkEvaluation(WorkEvaluation wa) {
		newWorkReport = null;
		newWorkEvaluation = wa;
	}

	@CommitAfter
	public void onSuccessFromFrmAddWorkEvaluation() {
		genericService.saveOrUpdate(newWorkEvaluation);
		newWorkReport = null;
		newWorkEvaluation = null;
	}

	public Float getProjectTotal() {
		return projectManager.sumPoints(project);
	}

	@Inject
	private SelectModelFactory selectModelFactory;

	public SelectModel getCoursesModel() {
		return selectModelFactory.create(getAllCourses(), "title");
	}

	public List<Course> getAllCourses() {
		return (List<Course>) genericService.getAll(Course.class);
	}

	@Persist
	@Property
	private Course selectedCourse;

}
