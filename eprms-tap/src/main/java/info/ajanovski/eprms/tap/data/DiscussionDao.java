package info.ajanovski.eprms.tap.data;

import java.util.List;

import info.ajanovski.eprms.model.entities.DiscussionPost;
import info.ajanovski.eprms.model.entities.DiscussionPostEvaluation;
import info.ajanovski.eprms.model.entities.DiscussionSession;
import info.ajanovski.eprms.model.entities.Person;

public interface DiscussionDao {

	List<Person> getPersonsActiveInDiscussionSession(DiscussionSession discussionSession);

	List<DiscussionPost> getPersonsDiscussionPostsInDiscussionSession(DiscussionSession discussionSession,
			Person person);

	List<DiscussionPostEvaluation> getDiscussionPostEvaluations(long discussionPostId);

}
