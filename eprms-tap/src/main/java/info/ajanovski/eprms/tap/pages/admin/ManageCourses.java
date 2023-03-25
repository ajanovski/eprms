package info.ajanovski.eprms.tap.pages.admin;

import java.util.ArrayList;
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
import info.ajanovski.eprms.model.entities.CourseTeacher;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.util.ActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.model.util.CourseActivityTypeHierarchicalComparator;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.CourseManager;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@AdministratorPage
@InstructorPage
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

	@Inject
	private CourseManager courseManager;

	@Property
	private Course course;

	@Property
	@Persist
	private Course addActivityTypeForCourse;

	@Property
	@Persist
	private Course addTeacherForCourse;

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
		return courseManager.getAllCoursesByPerson(userInfo.getPersonId());
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
	void onDeleteCourse(Course c) {
		genericService.delete(c);
	}

	void onActionFromAddCourseActivityType(Course c) {
		addActivityTypeForCourse = c;
	}

	void onActionFromAddCourseTeacher(Course c) {
		addTeacherForCourse = c;
	}

	@CommitAfter
	void onActionFromDeleteCourseActivityType(CourseActivityType cat) {
		genericService.delete(cat);
	}

	@CommitAfter
	void onActionFromDeleteCourseTeacher(CourseTeacher ct) {
		genericService.delete(ct);
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

	public void onActionFromAddCourse() {
		editCourse = new Course();
	}

	void onCancelEditCourse() {
		editCourse = null;
	}

	@Inject
	private PersonManager personManager;

	public SelectModel getTeacherModel() {
		return selectModelFactory.create(personManager.getAllPersonsFromRole(ModelConstants.RoleInstructor),
				"userName");
	}

	@Property
	private Person selectTeacher;

	public List<CourseTeacher> getCourseTeachers() {
		return course.getCourseTeachers();
	}

	@Property
	private CourseTeacher courseTeacher;

	void onCancelSelectTeacher() {
		addTeacherForCourse = null;
	}

	@CommitAfter
	public void onSuccessFromFrmAddTeacher() {
		CourseTeacher ct = new CourseTeacher();
		ct.setCourse(addTeacherForCourse);
		ct.setTeacher(selectTeacher);
		genericService.save(ct);
		addTeacherForCourse = null;
	}
}
