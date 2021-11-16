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

import info.ajanovski.eprms.model.entities.ActivityType;
import info.ajanovski.eprms.model.util.ActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;

@AdministratorPage
@InstructorPage
public class ManageActivityTypes {

	@Persist
	@Property
	private ActivityType newActivityType;

	@Property
	private ActivityType activityType;

	@Property
	private ActivityType activityType2;

	@Inject
	private GenericService genericService;

	public void onActionFromNewActivityType() {
		newActivityType = new ActivityType();
	}

	public void onActionFromEditActivityType(ActivityType at) {
		newActivityType = at;
	}

	@InjectComponent
	private Zone zoneActivityTypes;

	@CommitAfter
	public void onSuccessFromNewActivityTypeForm() {
		genericService.saveOrUpdate(newActivityType);
		newActivityType = null;
	}

	@Inject
	private SelectModelFactory selectModelFactory;

	public SelectModel getListTypes() {
		return selectModelFactory.create(genericService.getAll(ActivityType.class), "title");
	}

	public List<ActivityType> getAllActivityTypes() {
		ActivityTypeHierarchicalComparator athc = new ActivityTypeHierarchicalComparator();
		List<ActivityType> lista = (List<ActivityType>) genericService.getAll(ActivityType.class);
		lista.sort(athc);
		return lista;
	}

	@Persist
	@Property
	private ActivityType selectedActivityType;

	public Long getDepth(ActivityType at) {
		if (at.getSuperActivityType() != null) {
			return getDepth(at.getSuperActivityType()) + 1;
		} else {
			return 0L;
		}
	}

	public long getHierarchicalDepth() {
		return (3*getDepth(activityType));
	}

}
