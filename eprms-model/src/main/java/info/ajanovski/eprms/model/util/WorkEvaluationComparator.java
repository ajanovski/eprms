package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.WorkEvaluation;

public class WorkEvaluationComparator implements Comparator<WorkEvaluation> {
	public int compare(WorkEvaluation w1, WorkEvaluation w2) {
		return Long.valueOf(w1.getWorkEvaluationId()).compareTo(Long.valueOf(w2.getWorkEvaluationId()));
	}
}