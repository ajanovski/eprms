/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource 
 * Management System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *     
 * EPRMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 ******************************************************************************/

package info.ajanovski.eprms.tap.pages.user;

import java.security.MessageDigest;
import java.util.Base64;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.tap.annotations.AdministratorPage;
import info.ajanovski.eprms.tap.annotations.InstructorPage;
import info.ajanovski.eprms.tap.annotations.StudentPage;
import info.ajanovski.eprms.tap.services.GenericService;
import info.ajanovski.eprms.tap.util.UserInfo;

@StudentPage
@InstructorPage
@AdministratorPage
public class MyRepositoryAuth {

	@Property
	@SessionState
	private UserInfo userInfo;

	@Inject
	private GenericService genericService;

	@Property
	private String password;

	@Property
	private String confirmPassword;

	@InjectComponent("AuthForm")
	private Form authForm;

	@InjectComponent("confirmPassword")
	private PasswordField pfConfirmPassword;

	public void onValidateFromAuthForm() {
		if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
			authForm.recordError(pfConfirmPassword, "Enter two identical and non-empty passwords.");
		}
	}

	@CommitAfter
	public void onSuccessFromAuthForm() {
		if (password != null && confirmPassword != null && password.equals(confirmPassword)) {
			Person myself = genericService.getByPK(Person.class, userInfo.getPersonId());
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.reset();
				md.update(password.getBytes("UTF-8"));
				myself.setAuthString("{SHA}" + Base64.getEncoder().encodeToString(md.digest()));
				genericService.save(myself);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}
