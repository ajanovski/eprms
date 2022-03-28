package info.ajanovski.eprms.tap.pages.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.ActivityType;
import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.CourseActivityType;
import info.ajanovski.eprms.model.util.ActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.model.util.CourseActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.model.util.CourseComparator;
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
	private SelectModelFactory selectModelFactory;

	@Inject
	private GenericService genericService;

	@Property
	private Course course;
	@Property
	@Persist
	private Course addActivityTypeForCourse;
	@Property
	private ActivityType activityType;
	@Property
	private ActivityType selectParentActivityType;
	@Property
	private CourseActivityType courseActivityType;

	@Persist
	@Property
	private Course editCourse;

	@Persist
	@Property
	private List<ActivityType> inActivityTypes;

	public List<Course> getAllCourses() {
		List<Course> lista = (List<Course>) genericService.getAll(Course.class);
		CourseComparator cc = new CourseComparator();
		Collections.sort(lista, cc);
		return lista;
	}

	public List<CourseActivityType> getCourseCourseActivityTypes() {
		List<CourseActivityType> list = course.getCourseActivityTypes();

		CourseActivityTypeHierarchicalComparator comparator = new CourseActivityTypeHierarchicalComparator();
		list.sort(comparator);

		return list;
	}

	void onActionFromEditCourse(Course c) {
		editCourse = c;
	}

	@CommitAfter
	void onActionFromDeleteCourse(Course c) {
		genericService.delete(c);
	}

	void onActionFromAddCourseActivityType(Course c) {
		addActivityTypeForCourse = c;
	}

	@CommitAfter
	void onActionFromDeleteCourseActivityType(CourseActivityType cat) {
		genericService.delete(cat);
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

	public ValueEncoder<ActivityType> getActivityTypeEncoder() {
		return new ValueEncoder<ActivityType>() {
			@Override
			public String toClient(ActivityType value) {
				return String.valueOf(value.getActivityTypeId());
			}

			@Override
			public ActivityType toValue(String id) {
				return genericService.getByPK(ActivityType.class, Long.parseLong(id));
			}
		};
	}

	public void onActivate() {
		if (inActivityTypes == null) {
			inActivityTypes = new ArrayList<ActivityType>();
		}
	}

	public SelectModel getActivityTypeModel() {
		return selectModelFactory.create(getAllActivityTypes().stream().filter(at -> at.getSubActivityTypes() != null)
				.collect(Collectors.toList()), "title");
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
		return (3 * getDepth(courseActivityType.getActivityType()));
	}

	private boolean cancelFrmAddActivityType;

	void onSelectedFromSubmitSelectParentActivityType() {
		cancelFrmAddActivityType = false;
	}

	void onSelectedFromCancelSelectParentActivityType() {
		cancelFrmAddActivityType = true;
	}

	@CommitAfter
	public void onSuccessFromFrmAddActivityType() {
		if (!cancelFrmAddActivityType) {
			int pos = 1;
			for (ActivityType at : selectParentActivityType.getSubActivityTypes()) {
				CourseActivityType cat = new CourseActivityType();
				cat.setCourse(addActivityTypeForCourse);
				cat.setActivityType(at);
				cat.setPositionNumber(pos);
				pos++;
				genericService.saveOrUpdate(cat);
			}
			addActivityTypeForCourse = null;
		} else {
			addActivityTypeForCourse = null;
		}
	}

}
