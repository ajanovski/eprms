package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.model.entities.Role;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.PersonManager;
import info.ajanovski.eprms.tap.util.UserInfo;

@AdministratorPage
public class ManagePersons {

	@SessionState
	private UserInfo userInfo;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private PersonManager personManager;

	@Inject
	private GenericService genericService;

	@Inject
	private Logger logger;

	@InjectComponent
	private Form frmImport;

	private Boolean cancelForm = false;

	@Property
	private Person person;

	@Persist
	@Property
	private Person personToEdit;

	@Property
	@Persist
	private String personListToImport;

	@Property
	@Persist
	private String searchString;

	@Persist
	@Property
	private Person personToAddRole;

	@Persist
	@Property
	private Role selectRole;

	@Property
	private PersonRole personRole;

	@Persist
	@Property
	private String errors;

	public List<Person> getAllPersons() {
		List<Person> list = personManager.getAllPersons();
		if (searchString == null || searchString.equals("")) {
			return list;
		} else {
			return list.stream().filter(
					p -> (p.getFirstName() + p.getLastName() + p.getEmail() + p.getUserName()).contains(searchString))
					.collect(Collectors.toList());
		}
	}

	public void onActionFromImportPersons() {
		personListToImport = "firstName,lastName,email,userName";
	}

	@CommitAfter
	public void onSuccessFromFrmImport() {
		if (personListToImport != null) {
			errors = new String();
			Role r = (Role) genericService.getAll(Role.class).stream()
					.filter(role -> ((Role) role).getName().equals(ModelConstants.RoleStudent)).findFirst().get();
			if (r != null) {
				for (String line : personListToImport.split("\\r?\\n")) {
					logger.info(">>> Importing {} <<<", line);
					String[] lineFields = line.split("[,\t]");
					Person p;
					try {
						p = personManager.getPersonByUsername(lineFields[3]);
						if (p != null) {
							errors += ">>> Person " + p.getUserName()
									+ " already exists, skipping creation, activating.";
							p.setActive(true);
							genericService.saveOrUpdate(p);
						} else {
							p = new Person();
							p.setFirstName(lineFields[0]);
							p.setLastName(lineFields[1]);
							p.setEmail(lineFields[2]);
							p.setUserName(lineFields[3]);
							p.setActive(true);
							genericService.save(p);
							PersonRole pr = new PersonRole();
							pr.setPerson(p);
							pr.setRole(r);
							genericService.save(pr);
						}
					} catch (Exception e) {
						errors += ">>> Person " + lineFields[3] + " can not be imported due to: "
								+ e.getLocalizedMessage();
					}
				}
			} else {
				errors += "Role STUDENT does not exist";
			}
			if (!(errors.length() > 0)) {
				errors = "OK";
			}
		}
	}

	@CommitAfter
	public void onActionFromDeletePerson(Person p) {
		for (PersonRole pr : personManager.getPersonRolesForPerson(p.getPersonId())) {
			genericService.delete(pr);
		}
		genericService.delete(p);
	}

	@CommitAfter
	void onActionFromTogglePersonStatus(Person p) {
		p.setActive(!p.getActive());
		genericService.saveOrUpdate(p);
	}

	public void onActionFromNewPerson() {
		personToEdit = new Person();
	}

	public void onActionFromEditPerson(Person p) {
		personToEdit = p;
	}

	@CommitAfter
	public void saveChanges() {
		genericService.saveOrUpdate(personToEdit);
	}

	public void onCanceledFromFrmNewPerson() {
		cancelForm = true;
	}

	public void onValidateFromFrmNewPerson() {
		if (!cancelForm) {
			saveChanges();
		}
	}

	public void onSuccessFromFrmNewPerson() {
		personToEdit = null;
	}

	public SelectModel getRolesModel() {
		return selectModelFactory.create(genericService.getAll(Role.class), "name");
	}

	void onAddRole(Person p) {
		personToAddRole = p;
	}

	void onCancelAddRole() {
		personToAddRole = null;
	}

	public List<PersonRole> getPersonRoles() {
		return personManager.getPersonRolesForPerson(person.getPersonId());
	}

	@CommitAfter
	public void onSuccessFromFrmSelectRole() {
		PersonRole pr = new PersonRole();
		pr.setRole(selectRole);
		pr.setPerson(personToAddRole);
		genericService.save(pr);
		personToAddRole = null;
	}

	@CommitAfter
	public void onRemoveRole(PersonRole personRole) {
		genericService.delete(personRole);
	}

}
