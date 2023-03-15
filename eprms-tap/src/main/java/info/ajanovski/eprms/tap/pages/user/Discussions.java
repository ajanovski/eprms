package info.ajanovski.eprms.tap.pages.user;

import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.DiscussionOnCourseProject;
import info.ajanovski.eprms.model.entities.DiscussionPost;
import info.ajanovski.eprms.model.entities.DiscussionSession;
import info.ajanovski.eprms.model.entities.Responsibility;
import info.ajanovski.eprms.model.entities.TeamMember;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.CourseManager;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
public class Discussions {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Inject
	private CourseManager courseManager;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Persist
	@Property
	private Course selectedCourse;

	@Persist
	@Property
	private DiscussionSession selectedDiscussionSession;

	@Property
	private DiscussionOnCourseProject discussionOnCourseProject;

	@Property
	private DiscussionPost discussionPost;

	@Property
	private Responsibility responsibility;

	@Property
	private TeamMember teamMember;

	void onActivate() {
		if (selectedDiscussionSession != null) {
			selectedDiscussionSession = genericService.getByPK(DiscussionSession.class,
					selectedDiscussionSession.getDiscussionSessionId());
		}
	}

	public SelectModel getCourseModel() {
		return selectModelFactory.create(courseManager.getAllCoursesByPerson(userInfo.getPersonId()), "title");
	}

	public SelectModel getDiscussionSessionModel() {
		return selectModelFactory.create(((List<DiscussionSession>) genericService.getAll(DiscussionSession.class))
				.stream().filter(p -> p.getCourse().getCourseId() == selectedCourse.getCourseId()).toList(), "title");
	}

	public SelectModel getCourseProjectModel() {
		return selectModelFactory.create(selectedDiscussionSession.getDiscussionsOnCourseProjects(),
				"discussionOnCourseProjectId");
	}

	void onSuccessFromSelectCourseForm() {
		selectedDiscussionSession = null;
	}

}
