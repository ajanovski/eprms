package info.ajanovski.eprms.tap.services;

import java.util.List;

import info.ajanovski.eprms.model.entities.DiscussionPost;
import info.ajanovski.eprms.model.entities.DiscussionPostEvaluation;
import info.ajanovski.eprms.model.entities.DiscussionSession;
import info.ajanovski.eprms.model.entities.Person;

public interface DiscussionManager {

	public List<Person> getPersonsActiveInDiscussionSession(DiscussionSession discussionSession);

	public List<DiscussionPost> getPersonsDiscussionPostsInDiscussionSession(DiscussionSession discussionSession,
			Person person);

	public List<DiscussionPostEvaluation> getDiscussionPostEvaluations(long discussionPostId);

	public void createDiscussionPostEvaluation(String type, Person person, DiscussionPost dp);

}
