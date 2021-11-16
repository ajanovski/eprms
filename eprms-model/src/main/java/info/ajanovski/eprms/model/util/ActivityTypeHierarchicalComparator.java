package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.ActivityType;

public class ActivityTypeHierarchicalComparator implements Comparator<ActivityType> {
	public String getPath(ActivityType at) {
		if (at.getSuperActivityType() != null) {
			return getPath(at.getSuperActivityType()) + "." + at.getCode();
		} else {
			return at.getCode();
		}
	}

	public int compare(ActivityType a1, ActivityType a2) {
		return getPath(a1).compareTo(getPath(a2));
	}
}