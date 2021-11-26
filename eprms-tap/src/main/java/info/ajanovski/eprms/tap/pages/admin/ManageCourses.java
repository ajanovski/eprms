package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@AdministratorPage
public class ManageCourses {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private Logger logger;

	@Inject
	private GenericService genericService;

	@Property
	private Course course;

	@Persist
	@Property
	private Course editCourse;

	public List<Course> getAllCourses() {
		return (List<Course>) genericService.getAll(Course.class);
	}

	void onActionFromEditCourse(Course c) {
		editCourse = c;
	}

	@CommitAfter
	void onActionFromDeleteCourse(Course c) {
		genericService.delete(c);
	}

	public boolean isAllowDeleteCourse() {
		return (course.getCourseActivityTypes() == null || course.getCourseActivityTypes().size() == 0)
				&& (course.getCourseProjects() == null || course.getCourseProjects().size() == 0);
	}

	@CommitAfter
	void onSuccessFromFrmEditCourse() {
		genericService.saveOrUpdate(editCourse);
		editCourse = null;
	}

}
