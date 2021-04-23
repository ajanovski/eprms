package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;

@AdministratorPage
@InstructorPage
public class ManageRepositories {

	@Inject
	private GenericService genericService;

	@Property
	@Persist
	private Boolean isOnlyShowNotCreated;

	public void onActivate() {
		if (isOnlyShowNotCreated == null) {
			isOnlyShowNotCreated = false;
		}
	}

	@Property
	private Repository repository;

	public void onActionFromToggle() {
		isOnlyShowNotCreated = !isOnlyShowNotCreated;
	}

	public List<Repository> getRepositories() {
		List<Repository> ls = (List<Repository>) genericService.getAll(Repository.class);
		if (isOnlyShowNotCreated) {
			return ls.stream().filter(p -> p.getDateCreated() == null).collect(Collectors.toList());
		} else {
			return ls;
		}
	}
}
