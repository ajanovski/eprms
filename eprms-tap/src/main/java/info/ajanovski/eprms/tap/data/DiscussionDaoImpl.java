package info.ajanovski.eprms.tap.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import info.ajanovski.eprms.model.entities.DiscussionPost;
import info.ajanovski.eprms.model.entities.DiscussionPostEvaluation;
import info.ajanovski.eprms.model.entities.DiscussionSession;
import info.ajanovski.eprms.model.entities.Person;

public class DiscussionDaoImpl implements DiscussionDao {

	@Inject
	private Session session;

	private Session getEntityManager() {
		return session.getSession();
	}

	@Override
	public List<Person> getPersonsActiveInDiscussionSession(DiscussionSession discussionSession) {
		try {
			return getEntityManager().createQuery("""
					select distinct p
					from
						DiscussionPost dp
						join dp.person p
						join dp.discussionOnCourseProject dcp
						join dcp.discussionSession ds
					where ds.discussionSessionId=:discussionSessionId
					""").setParameter("discussionSessionId", discussionSession.getDiscussionSessionId())
					.getResultList();
		} catch (Exception e) {
			return new ArrayList<Person>();
		}
	}

	@Override
	public List<DiscussionPost> getPersonsDiscussionPostsInDiscussionSession(DiscussionSession discussionSession,
			Person person) {
		if (person != null) {
			try {
				return getEntityManager().createQuery("""
							select distinct dp
							from
								DiscussionPost dp
								join dp.person p
								join dp.discussionOnCourseProject docp
								join docp.discussionSession ds
							where ds.discussionSessionId=:discussionSessionId and
									p.personId=:personId
						""").setParameter("discussionSessionId", discussionSession.getDiscussionSessionId())
						.setParameter("personId", person.getPersonId()).getResultList();
			} catch (Exception e) {
				return new ArrayList<DiscussionPost>();
			}
		} else {
			return new ArrayList<DiscussionPost>();
		}
	}

	@Override
	public List<DiscussionPostEvaluation> getDiscussionPostEvaluations(long discussionPostId) {
		try {
			List<DiscussionPostEvaluation> l = getEntityManager().createQuery("""
						select dpe
						from
							DiscussionPostEvaluation dpe
							join dpe.discussionPost dp
						where dp.discussionPostId=:discussionPostId
						order by dpe.evaluatedOn
					""").setParameter("discussionPostId", discussionPostId).getResultList();
			if (l != null && l.size() > 0) {
				return l;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

}
