package info.ajanovski.eprms.tap.services;

import java.util.List;

import info.ajanovski.eprms.model.entities.Course;

public interface CourseManager {
	public List<Course> getAllCoursesByPerson(long personId);
}
