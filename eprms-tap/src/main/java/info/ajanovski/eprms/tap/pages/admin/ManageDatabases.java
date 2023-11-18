package info.ajanovski.eprms.tap.pages.admin;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.services.SystemConfigService;
import info.ajanovski.eprms.tap.util.AppConstants;

@AdministratorPage
@InstructorPage
public class ManageDatabases {

	@Inject
	private GenericService genericService;

	@Inject
	private SystemConfigService systemConfigService;

	@Property
	@Persist
	private Boolean isOnlyShowNotCreated;

	@Property
	private Database database;

	@Persist
	@Property
	private Database editDatabase;

	public void onActivate() {
		if (isOnlyShowNotCreated == null) {
			isOnlyShowNotCreated = false;
		}
	}

	public void onActionFromToggle() {
		isOnlyShowNotCreated = !isOnlyShowNotCreated;
	}

	public void onActionFromEdit(Database db) {
		editDatabase = db;
	}

	@CommitAfter
	public void onActionFromDelete(Database db) {
		if (db.getDateCreated() == null) {
			genericService.delete(db);
		}
	}

	@CommitAfter
	public void onActionFromActivateDatabases() {
		for (Database db : getDatabases()) {
			db.setDateCreated(new Date());
		}
	}

	@CommitAfter
	public void onSuccessFromEditDatabaseForm() {
		genericService.saveOrUpdate(editDatabase);
		editDatabase = null;
	}

	public List<Database> getDatabases() {
		List<Database> ls = (List<Database>) genericService.getAll(Database.class);
		if (isOnlyShowNotCreated) {
			return ls.stream().filter(p -> p.getDateCreated() == null).collect(Collectors.toList());
		} else {
			return ls;
		}
	}

	public String getDBCreationCommand() {
		return systemConfigService.getString(AppConstants.SystemParameterDBCreationCommand);
	}


	public String getDBSSHTunnelUserCreationCommand() {
		return systemConfigService.getString(AppConstants.SystemParameterDBSSHTunnelUserCreationCommand);
	}

}
