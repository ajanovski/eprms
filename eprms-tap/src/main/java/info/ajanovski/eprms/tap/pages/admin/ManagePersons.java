package info.ajanovski.eprms.tap.pages.admin;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
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
	private PersonManager personManager;

	@Inject
	private GenericService genericService;

	@Inject
	private Logger logger;

	@Property
	private Person person;

	@Property
	@Persist
	private String personListToImport;

	public List<Person> getAllPersons() {
		return personManager.getAllPersons();
	}

	public void onActionFromImportPersons() {
		personListToImport = "firstName,lastName,email,userName";
	}

	@InjectComponent
	private Form frmImport;

	@Persist
	@Property
	private String errors;

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
							errors += ">>> Person " + p.getUserName() + " already exists, skipping.";
						} else {
							p = new Person();
							p.setFirstName(lineFields[0]);
							p.setLastName(lineFields[1]);
							p.setEmail(lineFields[2]);
							p.setUserName(lineFields[3]);
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

	@Persist
	@Property
	private Person personToEdit;

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

	private Boolean cancelForm = false;

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

}
