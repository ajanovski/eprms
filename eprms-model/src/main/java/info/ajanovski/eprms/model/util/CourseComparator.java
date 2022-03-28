package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.Course;

public class CourseComparator implements Comparator<Course> {
	public int compare(Course a1, Course a2) {
		return a1.getTitle().compareTo(a2.getTitle());
	}
}