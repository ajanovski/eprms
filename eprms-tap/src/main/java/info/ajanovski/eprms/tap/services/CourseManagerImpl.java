package info.ajanovski.eprms.tap.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Course;
import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.util.CourseComparator;

public class CourseManagerImpl implements CourseManager {

	@Inject
	private PersonManager personManager;

	@Inject
	private GenericService genericService;

	@Override
	public List<Course> getAllCoursesByPerson(long personId) {
		List<Course> lista = (List<Course>) genericService.getAll(Course.class);
		if (personManager.isInstructor(personId) && !personManager.isAdministrator(personId)) {
			lista = lista.stream()
					.filter(p -> p.getCourseTeachers().stream().anyMatch(q -> q.getTeacher().getPersonId() == personId))
					.collect(Collectors.toList());
		}
		CourseComparator cc = new CourseComparator();
		Collections.sort(lista, cc);
		return lista;
	}

}
