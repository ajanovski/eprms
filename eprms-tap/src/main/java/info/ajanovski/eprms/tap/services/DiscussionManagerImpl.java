package info.ajanovski.eprms.tap.services;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.DiscussionPost;
import info.ajanovski.eprms.model.entities.DiscussionPostEvaluation;
import info.ajanovski.eprms.model.entities.DiscussionSession;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.tap.data.DiscussionDao;

public class DiscussionManagerImpl implements DiscussionManager {

	@Inject
	private DiscussionDao discussionDao;

	@Inject
	private GenericService genericService;

	@Override
	public List<Person> getPersonsActiveInDiscussionSession(DiscussionSession discussionSession) {
		return discussionDao.getPersonsActiveInDiscussionSession(discussionSession);
	}

	@Override
	public List<DiscussionPost> getPersonsDiscussionPostsInDiscussionSession(DiscussionSession discussionSession,
			Person person) {
		return discussionDao.getPersonsDiscussionPostsInDiscussionSession(discussionSession, person);
	}

	@Override
	public List<DiscussionPostEvaluation> getDiscussionPostEvaluations(long discussionPostId) {
		return discussionDao.getDiscussionPostEvaluations(discussionPostId);
	}

	@Override
	public void createDiscussionPostEvaluation(String type, Person person, DiscussionPost dp) {
		DiscussionPostEvaluation dpe = new DiscussionPostEvaluation();
		dpe.setDiscussionPost(genericService.getByPK(DiscussionPost.class, dp.getDiscussionPostId()));
		dpe.setEvaluatedOn(new Date());
		dpe.setPerson(genericService.getByPK(Person.class, person.getPersonId()));
		dpe.setMessage("");
		dpe.setPoints(null);
		dpe.setType(type);
		genericService.save(dpe);
	}

}
