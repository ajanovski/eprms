package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.Activity;

public class ActivityComparatorViaType implements Comparator<Activity> {
	public int compare(Activity a1, Activity a2) {
		return a1.getActivityType().getCode().compareTo(a2.getActivityType().getCode());
	}
}
