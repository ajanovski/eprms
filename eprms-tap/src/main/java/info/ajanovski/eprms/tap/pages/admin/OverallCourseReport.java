package info.ajanovski.eprms.tap.pages.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
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
import info.ajanovski.eprms.model.entities.CourseActivityType;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;
import info.ajanovski.eprms.model.util.CourseActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.model.util.ProjectCodeComparator;
import info.ajanovski.eprms.mq.MessagingService;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.components.ModalBox;
import info.ajanovski.eprms.tap.services.CourseManager;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.util.AppConfig;
import info.ajanovski.eprms.tap.util.AppConstants;
import info.ajanovski.eprms.tap.util.UserInfo;

@InstructorPage
@AdministratorPage
@Import(module = { "bootstrap/modal", "bootstrap/collapse",
		"zoneUpdateEffect" }, stylesheet = "OverallCourseReport.css")
public class OverallCourseReport {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private Logger logger;
	@Inject
	private MessagingService messagingService;
	@Inject
	private Request request;
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private GenericService genericService;
	@Inject
	private SystemConfigService systemConfigService;
	@Inject
	private PersonManager personManager;
	@Inject
	private ProjectManager projectManager;
	@Inject
	private CourseManager courseManager;

	@InjectComponent
	private Zone zWorkReport;
	@InjectComponent
	private Zone zTable;
	@InjectComponent
	private Zone zWorkEvaluation;
	@InjectComponent
	private Zone zAll;
	@InjectComponent
	private ModalBox createWorkEvaluationModal;
	@InjectComponent
	private ModalBox updateWorkEvaluationModal;
	@InjectComponent
	private ModalBox createWorkReportModal;
	@InjectComponent
	private ModalBox updateWorkReportModal;

	@Persist
	@Property
	private List<Project> projectsToHide;

	@Persist
	@Property
	private List<CourseActivityType> activitiesToHide;

	@Property
	private WorkEvaluation editorWorkEvaluation;

	@Property
	private Course selectedCourse;

	@Property
	private WorkReport editorWorkReport;

	@Property
	private CourseActivityType courseActivityType;

	@Property
	private WorkReport listedWorkReport;

	@Property
	private WorkEvaluation listedWorkEvaluation;

	@Property
	private Project hiddenProject;

	@Property
	private CourseActivityType hiddenActivity;

	@Property
	private Project project;

	public enum Mode {
		CREATEWORKREPORT, UPDATEWORKREPORT, CREATEEVALUATION, UPDATEEVALUATION, SELECTCOURSE
	}

	@InjectComponent
	private Form frmCreateWorkReport;
	@InjectComponent
	private Form frmUpdateWorkReport;
	@InjectComponent
	private Form frmCreateWorkEvaluation;
	@InjectComponent
	private Form frmUpdateWorkEvaluation;

	@Property
	private Mode editorMode;
	@Property
	private Long selectedCourseId;
	@Property
	private Long editorWorkReportId;
	@Property
	private Long editorWorkReportActivityId;
	@Property
	private Long editorWorkEvaluationId;
	@Property
	private Long editorWorkEvaluationWorkReportId;

	// Activate, Passivate

	void onActivate(EventContext ec) {
		if (ec.getCount() == 0) {
			editorMode = null;
			selectedCourseId = null;
			editorWorkReportId = null;
			editorWorkEvaluationId = null;
			editorWorkReportActivityId = null;
		} else if (ec.getCount() == 1) {
			selectedCourseId = ec.get(Long.class, 0);
			editorWorkReportId = null;
			editorWorkEvaluationId = null;
			editorWorkReportActivityId = null;
		} else {
			selectedCourseId = ec.get(Long.class, 0);
			editorMode = ec.get(Mode.class, 1);
			if (editorMode == Mode.SELECTCOURSE) {
			} else if (editorMode == Mode.CREATEWORKREPORT) {
				editorWorkReportActivityId = ec.get(Long.class, 2);
			} else if (editorMode == Mode.UPDATEWORKREPORT) {
				editorWorkReportId = ec.get(Long.class, 2);
			} else if (editorMode == Mode.CREATEEVALUATION) {
				editorWorkEvaluationWorkReportId = ec.get(Long.class, 2);
			} else if (editorMode == Mode.UPDATEEVALUATION) {
				editorWorkEvaluationId = ec.get(Long.class, 2);
			}
		}

		if (projectsToHide == null) {
			projectsToHide = new ArrayList<Project>();
		}
		if (activitiesToHide == null) {
			activitiesToHide = new ArrayList<CourseActivityType>();
		}
		if (selectedCourseId != null) {
			selectedCourse = genericService.getByPK(Course.class, selectedCourseId);
		}

		messagingService.setupMQHost(AppConfig.getString("MQHost"));
	}

	Object[] onPassivate() {
		if (editorMode == null) {
			return new Object[] { selectedCourseId };
		} else if (editorMode == Mode.CREATEWORKREPORT) {
			return new Object[] { selectedCourseId, editorMode, editorWorkReportActivityId };
		} else if (editorMode == Mode.UPDATEWORKREPORT) {
			return new Object[] { selectedCourseId, editorMode, editorWorkReportId };
		} else if (editorMode == Mode.CREATEEVALUATION) {
			return new Object[] { selectedCourseId, editorMode, editorWorkEvaluationWorkReportId };
		} else if (editorMode == Mode.UPDATEEVALUATION) {
			return new Object[] { selectedCourseId, editorMode, editorWorkEvaluationId };
		} else {
			throw new IllegalStateException(editorMode.toString());
		}
	}

	// Create Work Report

	public boolean isModeCreateWorkReport() {
		return editorMode == Mode.CREATEWORKREPORT;
	}

	void onActionFromCreateWorkReport(Activity a) {
		editorMode = Mode.CREATEWORKREPORT;
		editorWorkReportId = null;
		editorWorkReportActivityId = a.getActivityId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zWorkReport);
		}
	}

	void onActionFromCancelCreateWorkReport() {
		editorMode = null;
		editorWorkReportId = null;
		createWorkReportModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zWorkReport);
		}
	}

	void onPrepareForRenderFromFrmCreateWorkReport() throws Exception {
		if (frmCreateWorkReport.isValid()) {
			editorMode = Mode.CREATEWORKREPORT;
			editorWorkReport = new WorkReport();
			editorWorkReport
					.setTitle(systemConfigService.getString(AppConstants.SystemParameterTplNewWorkReportByAdmin));
		}
	}

	void onPrepareForSubmitFromFrmCreateWorkReport() throws Exception {
		editorMode = Mode.CREATEWORKREPORT;
		editorWorkReport = new WorkReport();
	}

	@CommitAfter
	void onValidateFromFrmCreateWorkReport() {
		if (frmCreateWorkReport.getHasErrors()) {
			return;
		}
		try {
			editorWorkReport.setActivity(genericService.getByPK(Activity.class, editorWorkReportActivityId));
			editorWorkReport.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
			Date d = new Date();
			editorWorkReport.setSubmissionDate(d);
			genericService.saveOrUpdate(editorWorkReport);
		} catch (Exception e) {
			frmCreateWorkReport.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmCreateWorkReport() {
		editorMode = null;
		editorWorkReportId = editorWorkReport.getWorkReportId();
		createWorkReportModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("zWorkReport", zWorkReport).addRender("zTable", zTable);
		}
	}

	// Update Work Report

	public boolean isWorkReportEditable() {
		if (listedWorkReport.getPerson().getPersonId() == userInfo.getPersonId()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isModeUpdateWorkReport() { // Update Work Report
		return editorMode == Mode.UPDATEWORKREPORT;
	}

	void onUpdateWorkReport(WorkReport wr) {
		editorMode = Mode.UPDATEWORKREPORT;
		editorWorkReportId = wr.getWorkReportId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zWorkReport);
		}
	}

	void onActionFromCancelUpdateWorkReport() {
		editorMode = null;
		editorWorkReportId = null;
		updateWorkReportModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onPrepareForRenderFromFrmUpdateWorkReport() {
		if (frmUpdateWorkReport.isValid()) {
			editorMode = Mode.UPDATEWORKREPORT;
			editorWorkReport = genericService.getByPK(WorkReport.class, editorWorkReportId);
		}
	}

	void onPrepareForSubmitFromFrmUpdateWorkReport() {
		editorMode = Mode.UPDATEWORKREPORT;
		editorWorkReport = genericService.getByPK(WorkReport.class, editorWorkReportId);
		if (editorWorkReport == null) {
			frmUpdateWorkReport.recordError("Param has been deleted by another process.");
			editorWorkReport = new WorkReport();
		}
	}

	@CommitAfter
	void onValidateFromFrmUpdateWorkReport() {
		if (frmUpdateWorkReport.getHasErrors()) {
			return;
		}
		try {
			genericService.saveOrUpdate(editorWorkReport);
		} catch (Exception e) {
			frmUpdateWorkReport.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmUpdateWorkReport() {
		editorMode = null;
		editorWorkReportId = editorWorkReport.getWorkReportId();
		updateWorkReportModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("zWorkReport", zWorkReport).addRender("zTable", zTable);
		}
	}

	////////////////////////////// End Work Report

	// Create Evaluation

	public boolean isModeCreateWorkEvaluation() {
		return editorMode == Mode.CREATEEVALUATION;
	}

	void onCreateWorkEvaluation(WorkReport wr) {
		editorMode = Mode.CREATEEVALUATION;
		editorWorkEvaluationId = null;
		editorWorkEvaluationWorkReportId = wr.getWorkReportId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zWorkEvaluation);
		}
	}

	void onActionFromCancelCreateWorkEvaluation() {
		editorMode = null;
		editorWorkEvaluationId = null;
		createWorkEvaluationModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zWorkEvaluation);
		}
	}

	void onPrepareForRenderFromFrmCreateWorkEvaluation() throws Exception {
		if (frmCreateWorkEvaluation.isValid()) {
			editorMode = Mode.CREATEEVALUATION;
			editorWorkEvaluation = new WorkEvaluation();
			editorWorkEvaluation
					.setWorkReport(genericService.getByPK(WorkReport.class, editorWorkEvaluationWorkReportId));
			editorWorkEvaluation.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
			editorWorkEvaluation
					.setTitle(systemConfigService.getString(AppConstants.SystemParameterTplNewWorkEvaluation));
			editorWorkEvaluation.setStatus(ModelConstants.EvaluationStatusCreated);
			Date d = new Date();
			editorWorkEvaluation.setEvaluationDate(d);
		}
	}

	void onPrepareForSubmitFromFrmCreateWorkEvaluation() throws Exception {
		editorMode = Mode.CREATEEVALUATION;
		editorWorkEvaluation = new WorkEvaluation();
	}

	@CommitAfter
	void onValidateFromFrmCreateWorkEvaluation() {
		if (frmCreateWorkEvaluation.getHasErrors()) {
			return;
		}
		try {
			editorWorkEvaluation
					.setWorkReport(genericService.getByPK(WorkReport.class, editorWorkEvaluationWorkReportId));
			editorWorkEvaluation.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
			genericService.saveOrUpdate(editorWorkEvaluation);
		} catch (Exception e) {
			frmCreateWorkReport.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmCreateWorkEvaluation() {
		editorMode = null;
		editorWorkEvaluationId = null;
		editorWorkEvaluationWorkReportId = null;
		//editorWorkEvaluationId = editorWorkEvaluation.getWorkEvaluationId();
		createWorkEvaluationModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("zWorkEvaluation", zWorkEvaluation).addRender("zTable", zTable);
		}
	}

	// Update Evaluation

	public boolean isWorkEvaluationEditable() {
		if (listedWorkEvaluation.getPerson().getPersonId() == userInfo.getPersonId()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isModeUpdateWorkEvaluation() { // Update Work Report
		return editorMode == Mode.UPDATEEVALUATION;
	}

	void onUpdateWorkEvaluation(WorkEvaluation we) {
		editorMode = Mode.UPDATEEVALUATION;
		editorWorkEvaluationId = we.getWorkEvaluationId();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zWorkEvaluation);
		}
	}

	void onActionFromCancelUpdateWorkEvaluation() {
		editorMode = null;
		editorWorkEvaluationId = null;
		updateWorkEvaluationModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll).addRender(zWorkEvaluation);
		}
	}

	void onPrepareForRenderFromFrmUpdateWorkEvaluation() {
		if (frmUpdateWorkEvaluation.isValid()) {
			editorMode = Mode.UPDATEEVALUATION;
			editorWorkEvaluation = genericService.getByPK(WorkEvaluation.class, editorWorkEvaluationId);
		}
	}

	void onPrepareForSubmitFromFrmUpdateWorkEvaluation() {
		editorMode = Mode.UPDATEEVALUATION;
		editorWorkEvaluation = genericService.getByPK(WorkEvaluation.class, editorWorkEvaluationId);
		if (editorWorkEvaluation == null) {
			frmUpdateWorkEvaluation.recordError("Param has been deleted by another process.");
			editorWorkEvaluation = new WorkEvaluation();
		}
	}

	@CommitAfter
	void onValidateFromFrmUpdateWorkEvaluation() {
		if (frmUpdateWorkEvaluation.getHasErrors()) {
			return;
		}
		try {
			genericService.saveOrUpdate(editorWorkEvaluation);
			// messagingService.sendWorkEvaluationNotification(newWorkEvaluation);
		} catch (Exception e) {
			frmUpdateWorkEvaluation.recordError(e.getMessage());
		}
	}

	void onSuccessFromFrmUpdateWorkEvaluation() {
		editorMode = null;
		editorWorkEvaluationId = editorWorkEvaluation.getWorkEvaluationId();
		updateWorkEvaluationModal.hide();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zTable);
		}
	}

	////////////////////////////// End Evaluation

	void onSuccessFromFrmSelectCourse() {
		selectedCourseId = selectedCourse.getCourseId();
	}

	/// End select course

	public List<Project> getListOfAllActiveProjects() {
		List<Project> list = new ArrayList<Project>();
		if (selectedCourse == null) {
			list = ((List<Project>) projectManager.getAllProjects()).stream()
					.filter(p -> (p.getStatus() != null && p.getStatus().equals(ModelConstants.ProjectStatusActive)))
					.collect(Collectors.toList());
		} else {
			list = ((List<Project>) projectManager.getAllProjectsInCourse(selectedCourse)).stream()
					.filter(p -> (p.getStatus() != null && p.getStatus().equals(ModelConstants.ProjectStatusActive)))
					.collect(Collectors.toList());
		}
		if (projectsToHide != null && projectsToHide.size() > 0) {
			list.removeIf(l -> projectsToHide.stream().anyMatch(ph -> ph.getProjectId() == l.getProjectId()));
		}
		list = list.stream().sorted(new ProjectCodeComparator()).toList();
		return list;
	}

	public List<CourseActivityType> getSelectedCourseCourseActivityTypes() {
		List<CourseActivityType> list = selectedCourse.getCourseActivityTypes();

		CourseActivityTypeHierarchicalComparator comparator = new CourseActivityTypeHierarchicalComparator();
		list.sort(comparator);

		if (activitiesToHide != null && activitiesToHide.size() > 0) {
			list.removeIf(l -> activitiesToHide.stream()
					.anyMatch(ah -> ah.getCourseActivityTypeId() == l.getCourseActivityTypeId()));
		}
		return list;
	}

	public Activity getActivity() {
		return project.getActivities().stream().filter(a -> a.getActivityType()
				.getActivityTypeId() == courseActivityType.getActivityType().getActivityTypeId()).findFirst()
				.orElse(null);
	}

	public List<WorkReport> getWorkReportsForActivity() {
		return projectManager.getWorkReportsForActivity(getActivity());
	}

	public List<WorkEvaluation> getWorkEvaluationsForWorkReport() {
		return projectManager.getWorkEvaluationForWorkReport(listedWorkReport);
	}

	public String getListedWorkReportPersonName() {
		return personManager.getPersonFullName(listedWorkReport.getPerson());
	}

	public String getListedWorkEvaluationPersonName() {
		return personManager.getPersonFullName(listedWorkEvaluation.getPerson());
	}

	@CommitAfter
	void onToggleWorkEvaluationStatus(WorkEvaluation wa) {
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

//	public String getZWorkEvaluationId() {
//		return "zWorkEvaluation_" + listedWorkEvaluation.getWorkEvaluationId();
//	}
//
//	public String getZWorkEvaluationIdNew() {
//		return "zWorkEvaluation_" + editorWorkEvaluation.getWorkEvaluationId();
//	}

	public List<Project> getListHiddenProjects() {
		return projectManager.orderProjectList(projectsToHide);
	}

	public Float getProjectTotal() {
		return projectManager.sumPoints(project);
	}

	public SelectModel getCoursesModel() {
		return selectModelFactory.create(getAllCourses(), "title");
	}

	public List<Course> getAllCourses() {
		return courseManager.getAllCoursesByPerson(userInfo.getPersonId());
	}

	void onActionFromResetListOfAllProjects() {
		projectsToHide.clear();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromResetListOfAllActivities() {
		activitiesToHide.clear();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromHideProjectFromListOfAllProjects(Project p) {
		projectsToHide.add(p);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromHideActivityFromListOfAllActivities(CourseActivityType cat) {
		activitiesToHide.add(cat);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromShowProject(Project p) {
		projectsToHide.removeIf(ph -> ph.getProjectId() == p.getProjectId());
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromShowActivity(CourseActivityType cat) {
		activitiesToHide.removeIf(ph -> ph.getCourseActivityTypeId() == cat.getCourseActivityTypeId());
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromHideAllProjects() {
		List<Project> lista = getListOfAllActiveProjects();
		projectsToHide.addAll(lista);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	void onActionFromHideAllActivities() {
		List<CourseActivityType> lista = getSelectedCourseCourseActivityTypes();
		activitiesToHide.addAll(lista);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAll);
		}
	}

	public String[] getEvalStatusModel() {
		return ModelConstants.AllEvaluationStatuses;
	}

	public String getPMProjectURLPrefix() {
		return systemConfigService.getString(AppConstants.SystemParameterPMProjectURLPrefix);
	}

	public String gethiddenActivityActivityTypeCode() {
		return genericService.getByPK(hiddenActivity.getClass(), hiddenActivity.getCourseActivityTypeId())
				.getActivityType().getCode();
	}
}
