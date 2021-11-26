package info.ajanovski.eprms.tap.pages.admin;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.CourseActivityType;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;
import info.ajanovski.eprms.model.util.CourseActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.mq.MessagingService;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.util.AppConfig;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
@Import(module = { "bootstrap/modal", "bootstrap/collapse", "zoneUpdateEffect" })
public class OverallCourseReport {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private Logger logger;

	@Inject
	private ProjectManager projectManager;

	@Inject
	private MessagingService messagingService;

	@Inject
	private GenericService genericService;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectComponent
	private Zone zNewWorkReportModal, zNewWorkEvaluationModal;

	public List<Project> getAllProjects() {
		if (selectedCourse == null) {
			return ((List<Project>) projectManager.getAllProjectsOrderByTitle()).stream()
					.filter(c -> c.getStatus() != null && c.getStatus().equals(ModelConstants.ProjectStatusActive))
					.collect(Collectors.toList());
		} else {
			return ((List<Project>) projectManager.getCourseProjectsOrderByTitle(selectedCourse)).stream()
					.filter(c -> c.getStatus() != null && c.getStatus().equals(ModelConstants.ProjectStatusActive))
					.collect(Collectors.toList());
		}
	}

	@Property
	private Project project;

	public Activity getActivity() {
		return project.getActivities().stream().filter(a -> a.getActivityType()
				.getActivityTypeId() == courseActivityType.getActivityType().getActivityTypeId()).findFirst()
				.orElse(null);
	}

	public List<CourseActivityType> getSelectedCourseCourseActivityTypes() {
		List<CourseActivityType> list = selectedCourse.getCourseActivityTypes();

		CourseActivityTypeHierarchicalComparator comparator = new CourseActivityTypeHierarchicalComparator();
		list.sort(comparator);

		return list;
	}

	@Property
	private CourseActivityType courseActivityType;

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
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNewWorkReportModal);
		}
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
		newWorkEvaluation.setEvaluationDate(new Date());
		newWorkEvaluation.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
		newWorkEvaluation.setStatus(ModelConstants.EvaluationStatusCreated);
		newWorkEvaluation.setWorkReport(wr);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNewWorkEvaluationModal);
		}
	}

	public void onActionFromEditWorkEvaluation(WorkEvaluation wa) {
		newWorkReport = null;
		newWorkEvaluation = wa;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNewWorkEvaluationModal);
		}
	}

	@CommitAfter
	public void onActionFromToggleWorkEvaluationStatus(WorkEvaluation wa) {
		if (wa.getStatus().equals(ModelConstants.EvaluationStatusCreated)) {
			wa.setStatus(ModelConstants.EvaluationStatusPublished);
		} else {
			wa.setStatus(ModelConstants.EvaluationStatusCreated);
		}
		genericService.saveOrUpdate(wa);
	}

	@CommitAfter
	public void onSuccessFromFrmAddWorkEvaluation() {
		genericService.saveOrUpdate(newWorkEvaluation);
		messagingService.sendWorkEvaluationNotification(newWorkEvaluation);
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

	public void onActivate() {
		if (selectedCourse != null) {
			selectedCourse = genericService.getByPK(Course.class, selectedCourse.getCourseId());
		}
		messagingService.setupMQHost(AppConfig.getString("MQHost"));
	}

}
