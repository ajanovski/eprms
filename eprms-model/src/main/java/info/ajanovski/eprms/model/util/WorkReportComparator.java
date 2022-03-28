package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.WorkReport;

public class WorkReportComparator implements Comparator<WorkReport> {
	public int compare(WorkReport w1, WorkReport w2) {
		return Long.valueOf(w1.getWorkReportId()).compareTo(Long.valueOf(w2.getWorkReportId()));
	}
}