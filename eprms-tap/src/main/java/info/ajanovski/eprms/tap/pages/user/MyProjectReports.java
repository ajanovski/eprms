package info.ajanovski.eprms.tap.pages.user;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;
import info.ajanovski.eprms.model.util.ActivityComparatorViaType;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.ProjectManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
@Import(module = { "bootstrap/modal", "bootstrap/collapse", "zoneUpdateEffect" })
public class MyProjectReports {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private Logger logger;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectComponent
	private Zone zModal;

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
	private WorkReport workReport;

	@Property
	private WorkEvaluation workEvaluation;

	@Persist
	@Property
	private WorkReport newWorkReport;

	public void onAddReport(Activity a) {
		newWorkReport = new WorkReport();
		newWorkReport.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
		newWorkReport.setActivity(a);
		newWorkReport.setSubmissionDate(new Date());
	}

	public void onEditWorkReport(WorkReport wr) {
		newWorkReport = wr;
	}

	@CommitAfter
	private void saveChangesToReport() {
		genericService.saveOrUpdate(newWorkReport);
	}

	private Boolean cancelForm = false;

	public void onCanceledFromFrmNewWorkReport() {
		cancelForm = true;
	}

	public void onValidateFromFrmNewWorkReport() {
		if (!cancelForm) {
			saveChangesToReport();
		}
	}

	public void onSuccessFromFrmNewWorkReport() {
		newWorkReport = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zModal);
		}
	}

	@CommitAfter
	public void onDeleteWorkReport(WorkReport wr) {
		genericService.delete(wr);
		newWorkReport = null;
	}

	public List<Activity> getProjectActivities() {
		ActivityComparatorViaType acr = new ActivityComparatorViaType();
		List<Activity> lista = project.getActivities();
		lista.sort(acr);
		return lista;
	}

	public boolean isEvaluationPublished() {
		return workEvaluation.getStatus().equals(ModelConstants.EvaluationStatusPublished);
	}

	public List<WorkReport> getWorkReportsForActivity() {
		return projectManager.getWorkReportsForActivity(activity);
	}

	public List<WorkEvaluation> getWorkEvaluationsForWorkReport() {
		return projectManager.getWorkEvaluationForWorkReport(workReport);
	}

	public String getProjectURL() {
		return projectManager.getProjectURL(project);
	}

}
