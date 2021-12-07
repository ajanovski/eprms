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

	@InjectComponent
	private Zone zoneActivityTypes;

	@Inject
	private GenericService genericService;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Property
	private ActivityType activityType;

	@Persist
	@Property
	private ActivityType newActivityType;

	public void onActionFromNewActivityType() {
		newActivityType = new ActivityType();
	}

	public void onActionFromEditActivityType(ActivityType at) {
		newActivityType = at;
	}

	public SelectModel getListTypes() {
		return selectModelFactory.create(genericService.getAll(ActivityType.class), "title");
	}

	public List<ActivityType> getAllActivityTypes() {
		ActivityTypeHierarchicalComparator athc = new ActivityTypeHierarchicalComparator();
		List<ActivityType> lista = (List<ActivityType>) genericService.getAll(ActivityType.class);
		lista.sort(athc);
		return lista;
	}

	public Long getDepth(ActivityType at) {
		if (at.getSuperActivityType() != null) {
			return getDepth(at.getSuperActivityType()) + 1;
		} else {
			return 0L;
		}
	}

	public long getHierarchicalDepth() {
		return (3 * getDepth(activityType));
	}

	public void onActivate() {
		if (newActivityType != null) {
			newActivityType = genericService.getByPK(ActivityType.class, newActivityType.getActivityTypeId());

		}
	}

	@CommitAfter
	public void onSuccessFromNewActivityTypeForm() {
		genericService.saveOrUpdate(newActivityType);
		newActivityType = null;
	}

}
