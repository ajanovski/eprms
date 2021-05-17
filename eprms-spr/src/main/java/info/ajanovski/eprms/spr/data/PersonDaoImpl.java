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

package info.ajanovski.eprms.spr.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.model.entities.Role;

@Service
public class PersonDaoImpl implements PersonDao {
	private final Logger logger = LoggerFactory.getLogger(PersonDaoImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Person> getAllPersons() {
		try {
			return (List<Person>) entityManager.createQuery("from Person order by lastName, firstName, userName")
					.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Person getPersonByUsername(String username) {
		try {
			return (Person) entityManager.createQuery("from Person where userName=:userName")
					.setParameter("userName", username).getSingleResult();
		} catch (Exception e) {
			logger.error("Person with userName:{} was not found.", username);
			return null;
		}
	}

	@Override
	public List<Person> getPersonByFilter(String filter) {
		String f = "%" + filter.toLowerCase() + "%";
		return entityManager
				.createQuery("select p from Person p  where (lower(concat(userName,firstName,lastName)) like :filter)")
				.setParameter("filter", f).getResultList();
	}

	@Override
	public List<PersonRole> getPersonRolesForPerson(long personId) {
		return entityManager.createQuery("select pr from PersonRole pr " + "join pr.role r " + "join pr.person p "
				+ "where p.personId=:personId").setParameter("personId", personId).getResultList();
	}

	@Override
	public List<Role> getRolesForPerson(long personId) {
		return entityManager.createQuery("select pr.role from PersonRole pr where pr.person.personId=:personId")
				.setParameter("personId", personId).getResultList();
	}

	@Override
	public String getPersonFullName(Person person) {
		return person.getLastName() + " " + person.getFirstName();
	}

	@Override
	public String getPersonFullNameWithId(Person person) {
		return person.getLastName() + " " + person.getFirstName() + " [" + person.getUserName() + "]";
	}

}
