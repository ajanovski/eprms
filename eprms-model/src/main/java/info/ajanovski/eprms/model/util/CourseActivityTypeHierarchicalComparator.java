package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.ActivityType;
import info.ajanovski.eprms.model.entities.CourseActivityType;

public class CourseActivityTypeHierarchicalComparator implements Comparator<CourseActivityType> {
	public String getPath(ActivityType at) {
		if (at.getSuperActivityType() != null) {
			return getPath(at.getSuperActivityType()) + "." + at.getCode();
		} else {
			return at.getCode();
		}
	}

	public int compare(CourseActivityType a1, CourseActivityType a2) {
		return getPath(a1.getActivityType()).compareTo(getPath(a2.getActivityType()));
	}
}