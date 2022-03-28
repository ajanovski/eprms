package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.Activity;
import info.ajanovski.eprms.model.entities.Project;
import info.ajanovski.eprms.model.entities.WorkEvaluation;
import info.ajanovski.eprms.model.entities.WorkReport;

public class ProjectActiveComparator implements Comparator<Project> {
	public Float mostActive(Project p) {
		Float sum = 0F;
		for (Activity a : p.getActivities()) {
			Float max = 0F;
			for (WorkReport wr : a.getWorkReports()) {
				for (WorkEvaluation we : wr.getWorkEvaluations()) {
					if (we.getPoints() > max) {
						max = we.getPoints();
					}
				}
			}
			sum += max;
		}
		return sum;
	}

	public int compare(Project p1, Project p2) {
		return mostActive(p1).compareTo(mostActive(p2));
	}
}