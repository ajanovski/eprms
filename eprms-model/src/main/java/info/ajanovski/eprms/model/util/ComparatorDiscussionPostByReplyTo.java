package info.ajanovski.eprms.model.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;

import info.ajanovski.eprms.model.entities.DiscussionPost;

public class ComparatorDiscussionPostByReplyTo implements Comparator<DiscussionPost> {

	public String getCoding(DiscussionPost i) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (i.getReplyTo() == null) {
			return dt.format(i.getPostedOn()) + "-";
		} else {
			return getCoding(i.getReplyTo()) + dt.format(i.getPostedOn()) + "-";
		}
	}

	@Override
	public int compare(DiscussionPost o1, DiscussionPost o2) {
		String hier1 = getCoding(o1);
		String hier2 = getCoding(o2);
		return hier1.compareTo(hier2);
	}

}
