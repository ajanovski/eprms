package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.DiscussionPost;

public class ComparatorDiscussionPostByReplyTo implements Comparator<DiscussionPost> {

	public String getCoding(DiscussionPost i) {
		if (i.getReplyTo() == null) {
			return Long.toString(i.getPostedOn().getTime()) + "-";
		} else {
			return getCoding(i.getReplyTo()) + Long.toString(i.getPostedOn().getTime()) + "-";
		}
	}

	@Override
	public int compare(DiscussionPost o1, DiscussionPost o2) {
		String hier1 = getCoding(o1);
		String hier2 = getCoding(o2);
		return hier1.compareTo(hier2);
	}

}
