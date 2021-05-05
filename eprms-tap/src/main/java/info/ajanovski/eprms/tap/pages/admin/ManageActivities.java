package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.services.SelectModelFactory;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.ActivityType;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.services.GenericService;

@AdministratorPage
public class ManageActivities {

	@Persist
	@Property
	private Project selectedProject;

	@Persist
	@Property
	private Activity newActivity;

	@Inject
	private GenericService genericService;

	public void onActivate() {
		if (selectedProject != null) {
			selectedProject = genericService.getByPK(Project.class, selectedProject.getProjectId());
		}
	}

	public void onActivate(Project p) {
		selectedProject = genericService.getByPK(Project.class, p.getProjectId());
	}

	public void onActionFromNewActivity() {
		newActivity = new Activity();
		newActivity.setProject(selectedProject);
	}

	@InjectComponent
	private Zone zoneActivities;

	@CommitAfter
	public void onSuccessFromNewActivityForm() {
		genericService.save(newActivity);
		newActivity = null;
	}

	@Inject
	private SelectModelFactory selectModelFactory;

	public SelectModel getListTypes() {
		return selectModelFactory.create(genericService.getAll(ActivityType.class), "title");
	}

	public List<Activity> getAllActivities() {
		return selectedProject.getActivities();
	}

}
