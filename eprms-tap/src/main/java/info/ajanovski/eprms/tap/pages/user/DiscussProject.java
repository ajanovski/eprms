package info.ajanovski.eprms.tap.pages.user;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import info.ajanovski.eprms.model.entities.DiscussionOnCourseProject;
import info.ajanovski.eprms.model.entities.DiscussionPost;
import info.ajanovski.eprms.model.entities.DiscussionPostEvaluation;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.util.ComparatorDiscussionPostByReplyTo;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.DiscussionManager;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.util.AppConstants;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@Import(stylesheet = { "DiscussProject.css" })
public class DiscussProject {
	@SessionState
	@Property
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Inject
	private SystemConfigService systemConfigService;

	@Inject
	private PersonManager personManager;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private Request request;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Property
	DiscussionOnCourseProject discussionOnCourseProject;

	@Property
	DiscussionPost discussionPost;

	@Property
	DiscussionPostEvaluation discussionPostEvaluation;

	@Persist
	@Property
	DiscussionPost editDiscussionPost;

	@InjectComponent
	private Zone zAllPosts;

	void onActivate(DiscussionOnCourseProject docp) {
		discussionOnCourseProject = genericService.getByPK(DiscussionOnCourseProject.class,
				docp.getDiscussionOnCourseProjectId());
	}

	public DiscussionOnCourseProject onPassivate() {
		return discussionOnCourseProject;
	}

	public String[] getPostTypes() {
		return ModelConstants.AllDiscussionPostTypes;
	}

	public List<DiscussionPost> getAllDiscussionPosts() {
		List<DiscussionPost> lista = discussionOnCourseProject.getDiscussionPosts();
		ComparatorDiscussionPostByReplyTo c = new ComparatorDiscussionPostByReplyTo();
		Collections.sort(lista, c);
		return lista;
	}

	void onActionFromAddPost() {
		editDiscussionPost = new DiscussionPost();
		editDiscussionPost.setDiscussionOnCourseProject(discussionOnCourseProject);
		editDiscussionPost.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	void onActionFromReplyToPost(DiscussionPost originalPost) {
		editDiscussionPost = new DiscussionPost();
		editDiscussionPost.setReplyTo(originalPost);
		editDiscussionPost.setType(originalPost.getType());
		editDiscussionPost.setDiscussionOnCourseProject(discussionOnCourseProject);
		editDiscussionPost.setPerson(genericService.getByPK(Person.class, userInfo.getPersonId()));
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onSuccessFromFrmEditPost() {
		editDiscussionPost.setPostedOn(new Date());
		genericService.saveOrUpdate(editDiscussionPost);
		editDiscussionPost = null;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	public String getCoding(DiscussionPost i) {
		if (i.getReplyTo() == null) {
			return Long.toString(i.getPostedOn().getTime()) + "-";
		} else {
			return getCoding(i.getReplyTo()) + Long.toString(i.getPostedOn().getTime()) + "-";
		}
	}

	public String getIndentation() {
		int a = getCoding(discussionPost).split("-").length;
		return "indentation" + a;
	}

	public boolean isPostAuthorProjectTeamMember() {
		return discussionPost.getDiscussionOnCourseProject().getCourseProject().getProject().getResponsibilities()
				.stream().anyMatch(r -> r.getTeam().getTeamMembers().stream()
						.anyMatch(tm -> tm.getPerson().getPersonId() == discussionPost.getPerson().getPersonId()));
	}

	public String getPostAuthor() {
		String fullName = personManager.getPersonFullNameWithId(discussionPost.getPerson());
		if (personManager.isInstructor(discussionPost.getPerson().getPersonId())) {
			return fullName;
		} else if (isPostAuthorProjectTeamMember()) {
			return fullName;
		} else if (discussionPost.getPublicPosting()) {
			return fullName;
		} else {
			if (userInfo.isInstructor()) {
				return "Anon" + " - " + fullName;
			} else {
				return "Anon";
			}
		}
	}

	public String getClassOfPostAuthor() {
		if (personManager.isInstructor(discussionPost.getPerson().getPersonId())) {
			return "border-primary personAdmin";
		} else if (personManager.isAdministrator(discussionPost.getPerson().getPersonId())) {
			return "border-primary personAdmin";
		} else if (isPostAuthorProjectTeamMember()) {
			return "border-dark personProjectTeamMember";
		} else {
			return "border-light";
		}
	}

	public String getRoleOfPostAuthor() {
		if (personManager.isInstructor(discussionPost.getPerson().getPersonId())) {
			return ModelConstants.RoleInstructor;
		} else if (personManager.isAdministrator(discussionPost.getPerson().getPersonId())) {
			return ModelConstants.RoleAdministrator;
		} else if (isPostAuthorProjectTeamMember()) {
			return ModelConstants.TeamMemberRoleMember;
		} else {
			return "";
		}
	}

	public String getIconOfPostAuthor() {
		if (personManager.isInstructor(discussionPost.getPerson().getPersonId())) {
			return "award";
		} else if (personManager.isAdministrator(discussionPost.getPerson().getPersonId())) {
			return "cpu";
		} else if (isPostAuthorProjectTeamMember()) {
			return "users";
		} else {
			return "";
		}
	}

	void onActionFromCancelFrmEditPost() {
		editDiscussionPost = null;
	}

	@Inject
	private DiscussionManager discussionManager;

	public List<DiscussionPostEvaluation> getDiscussionPostEvaluations() {
		return discussionManager.getDiscussionPostEvaluations(discussionPost.getDiscussionPostId());
	}

	public void createDiscussionPostEvaluation(String type, DiscussionPost dp) {
		discussionManager.createDiscussionPostEvaluation(type,
				genericService.getByPK(Person.class, userInfo.getPersonId()), dp);
	}

	@CommitAfter
	public void onActionFromEvaluatePostFunctionality(DiscussionPost discussionPost) {
		createDiscussionPostEvaluation(ModelConstants.DiscussionPostEvaluationTypeFunctionality, discussionPost);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromEvaluatePostBug(DiscussionPost discussionPost) {
		createDiscussionPostEvaluation(ModelConstants.DiscussionPostEvaluationTypeBug, discussionPost);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromEvaluatePostModel(DiscussionPost discussionPost) {
		createDiscussionPostEvaluation(ModelConstants.DiscussionPostEvaluationTypeModel, discussionPost);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromEvaluatePostIdea(DiscussionPost discussionPost) {
		createDiscussionPostEvaluation(ModelConstants.DiscussionPostEvaluationTypeIdea, discussionPost);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromEvaluatePostOther(DiscussionPost discussionPost) {
		createDiscussionPostEvaluation(ModelConstants.DiscussionPostEvaluationTypeOther, discussionPost);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromDeleteDiscussionPostEvaluation(DiscussionPostEvaluation discussionPostEvaluation) {
		genericService.delete(discussionPostEvaluation);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromAcceptDiscussionPostEvaluation(DiscussionPostEvaluation discussionPostEvaluation) {
		discussionPostEvaluation.setAccepted(true);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromRejectDiscussionPostEvaluation(DiscussionPostEvaluation discussionPostEvaluation) {
		discussionPostEvaluation.setAccepted(false);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromSetEvaluatedPostingAsATeamDiscussionPostEvaluation(
			DiscussionPostEvaluation discussionPostEvaluation) {
		if (discussionPostEvaluation.getEvaluatePostingAsATeam() != null) {
			discussionPostEvaluation.setEvaluatePostingAsATeam(!discussionPostEvaluation.getEvaluatePostingAsATeam());
		} else {
			discussionPostEvaluation.setEvaluatePostingAsATeam(true);
		}
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromSetEvaluatedPostingAddPoints(DiscussionPostEvaluation discussionPostEvaluation) {
		if (discussionPostEvaluation.getPoints() == null) {
			discussionPostEvaluation.setPoints(1);
		} else {
			discussionPostEvaluation.setPoints(discussionPostEvaluation.getPoints() + 1);
		}
		genericService.saveOrUpdate(discussionPostEvaluation);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	@CommitAfter
	public void onActionFromSetEvaluatedPostingSubtractPoints(DiscussionPostEvaluation discussionPostEvaluation) {
		if (discussionPostEvaluation.getPoints() != null) {
			if (discussionPostEvaluation.getPoints() > 0) {
				discussionPostEvaluation.setPoints(discussionPostEvaluation.getPoints() - 1);
			} else {
				discussionPostEvaluation.setPoints(null);
			}
		}
		genericService.saveOrUpdate(discussionPostEvaluation);
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zAllPosts);
		}
	}

	public String getPMProjectURLPrefix() {
		return systemConfigService.getString(AppConstants.SystemParameterPMProjectURLPrefix);
	}
}
