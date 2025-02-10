package info.ajanovski.eprms.model.util;

import java.util.Comparator;

import info.ajanovski.eprms.model.entities.Project;

public class ProjectCodeComparator implements Comparator<Project> {
	public int compare(Project p1, Project p2) {
		return p1.getCode().toLowerCase().compareTo(p2.getCode().toLowerCase());
	}
}