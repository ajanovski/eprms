package info.ajanovski.eprms.tap.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

import info.ajanovski.eprms.model.entities.ActivityType;

public class ActivityTypeSelectModel extends AbstractSelectModel {
	private List<ActivityType> activityTypes;

	public ActivityTypeSelectModel(List<ActivityType> activityTypes) {
		if (activityTypes == null) {
			this.activityTypes = new ArrayList<ActivityType>();
		} else {
			this.activityTypes = activityTypes;
		}
	}

	@Override
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	public int getDepth(ActivityType at) {
		if (at.getSuperActivityType() != null) {
			return getDepth(at.getSuperActivityType()) + 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
		for (ActivityType at : activityTypes) {
			String name = (new String("-")).repeat(3 * getDepth(at)) + at.getTitle();
			options.add(new OptionModelImpl(name, at));
		}
		return options;
	}
}