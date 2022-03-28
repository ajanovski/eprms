package info.ajanovski.eprms.tap.pages.admin;

import java.util.ArrayList;
import java.util.Collections;
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
import info.ajanovski.eprms.model.util.CourseComparator;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.model.util.WorkEvaluationComparator;
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

	@Inject
	private SelectModelFactory selectModelFactory;

	@InjectComponent
	private Zone zNewWorkReportModal, zNewWorkEvaluationModal, zTable;

	@Persist
	@Property
	private Course selectedCourse;

	@Property
	private CourseActivityType courseActivityType;

	@Property
	private WorkReport workReport;

	@Property
	private WorkEvaluation workEvaluation;

	@Persist
	@Property
	private WorkReport newWorkReport;

	@Persist
	@Property
	private WorkEvaluation newWorkEvaluation;

	public List<Project> getAllProjects() {
		List<Project> list = new ArrayList<Project>();
		if (selectedCourse == null) {
			list = ((List<Project>) projectManager.getAllProjectsOrderByTitle()).stream()
					.filter(p -> (p.getStatus() != null && p.getStatus().equals(ModelConstants.ProjectStatusActive)))
					.collect(Collectors.toList());
		} else {
			list = ((List<Project>) projectManager.getAllProjectsInCourseOrderByTitle(selectedCourse)).stream()
					.filter(p -> (p.getStatus() != null && p.getStatus().equals(ModelConstants.ProjectStatusActive)))
					.collect(Collectors.toList());
		}
		return list;
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

	public List<WorkReport> getWorkReportsForActivity() {
		return projectManager.getWorkReportsForActivity(getActivity());
	}

	public List<WorkEvaluation> getWorkEvaluationsForWorkReport() {
		return projectManager.getWorkEvaluationForWorkReport(workReport);
	}

	public void onAddWorkReport(Activity a) {
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

	public void onAddWorkEvaluation(WorkReport wr) {
		WorkReport wr1 = genericService.getByPK(WorkReport.class, wr.getWorkReportId());
		newWorkReport = null;
		newWorkEvaluation = new WorkEvaluation();
		newWorkEvaluation.setEvaluationDate(new Date());
		newWorkEvaluation.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
		newWorkEvaluation.setStatus(ModelConstants.EvaluationStatusCreated);
		newWorkEvaluation.setWorkReport(wr1);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNewWorkEvaluationModal);
		}
	}

	public void onEditWorkEvaluation(WorkEvaluation wa) {
		newWorkReport = null;
		newWorkEvaluation = wa;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zNewWorkEvaluationModal);
		}
	}

	@CommitAfter
	public void onToggleWorkEvaluationStatus(WorkEvaluation wa) {
		if (wa.getStatus().equals(ModelConstants.EvaluationStatusCreated)) {
			wa.setStatus(ModelConstants.EvaluationStatusPublished);
		} else {
			wa.setStatus(ModelConstants.EvaluationStatusCreated);
		}
		genericService.saveOrUpdate(wa);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zTable);
		}
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

	public SelectModel getCoursesModel() {
		return selectModelFactory.create(getAllCourses(), "title");
	}

	public List<Course> getAllCourses() {
		List<Course> lista = (List<Course>) genericService.getAll(Course.class);
		CourseComparator cc = new CourseComparator();
		Collections.sort(lista, cc);
		return lista;
	}

	public void onActivate() {
		if (selectedCourse != null) {
			selectedCourse = genericService.getByPK(Course.class, selectedCourse.getCourseId());
		}
		if (newWorkEvaluation != null && newWorkEvaluation.getWorkReport() != null) {
			newWorkEvaluation.setWorkReport(
					genericService.getByPK(WorkReport.class, newWorkEvaluation.getWorkReport().getWorkReportId()));
		}
		messagingService.setupMQHost(AppConfig.getString("MQHost"));
	}

}
