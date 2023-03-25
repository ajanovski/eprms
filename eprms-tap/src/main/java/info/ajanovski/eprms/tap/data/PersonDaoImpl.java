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
 * 
 ******************************************************************************/

package info.ajanovski.eprms.tap.data;

import java.util.List;

import javax.persistence.Query;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.model.entities.Role;
import info.ajanovski.eprms.tap.util.UsefulMethods;

public class PersonDaoImpl implements PersonDao {
	@Inject
	private Logger logger;

	@Inject
	private Session session;

	private Session getEntityManager() {
		return session.getSession();
	}

	@Override
	public List<Person> getAllPersons() {
		try {
			return UsefulMethods.castList(Person.class,
					getEntityManager().createQuery("from Person order by lastName").getResultList());
		} catch (DataException e) {
			logger.error("Exception - DataAccessException occurs : {} on complete getAllPersons().", e.getMessage());
			return null;
		}
	}

	@Override
	public Person getPersonByUsername(String username) {
		return (Person) getEntityManager().createQuery("from Person where userName=:username")
				.setParameter("username", username).setReadOnly(true).setCacheable(true).uniqueResult();
	}

	@Override
	public List<Person> getPersonByFilter(String filter) {
		String f = "%" + filter.toLowerCase() + "%";
		Query q = getEntityManager()
				.createQuery("select p from Person p  where (lower(concat(userName,firstName,lastName)) like :filter)");
		q.setParameter("filter", f);
		return UsefulMethods.castList(Person.class, q.getResultList());
	}

	@Override
	public List<PersonRole> getPersonRolesForPerson(long personId) {
		return (List<PersonRole>) getEntityManager()
				.createQuery("from PersonRole pr where pr.person.personId=:personId").setParameter("personId", personId)
				.getResultList();
	}

	@Override
	public String getPersonFullName(Person person) {
		return person.getLastName() + " " + person.getFirstName();
	}

	@Override
	public String getPersonFullNameWithId(Person person) {
		return person.getLastName() + " " + person.getFirstName() + " [" + person.getUserName() + "]";
	}

	@Override
	public List<Role> getRolesForPerson(long personId) {
		return getEntityManager().createQuery("select pr.role from PersonRole pr where pr.person.personId=:personId")
				.setParameter("personId", personId).getResultList();
	}

	@Override
	public List<Person> getAllPersonsFromRole(String roleName) {
		return getEntityManager().createQuery("select pr.person from PersonRole pr where pr.role.name=:roleName")
				.setParameter("roleName", roleName).getResultList();
	}

}
