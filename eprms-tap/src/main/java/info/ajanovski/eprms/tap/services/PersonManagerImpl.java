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

package info.ajanovski.eprms.tap.services;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.tap.data.PersonDao;

public class PersonManagerImpl implements PersonManager {

        @Inject
        private GenericService genericService;

        @Inject
        private PersonDao personDao;

        @Override
        public List<Person> getAllPersons() {
                return this.personDao.getAllPersons();
        }

        @Override
        public Person getPersonByUsername(String username) {
                return this.personDao.getPersonByUsername(username);
        }

        public Person getPersonById(long personId) {
                return genericService.getByPK(Person.class, personId);
        }

        @Override
        public List<Person> getPersonByFilter(String filter) {
                return personDao.getPersonByFilter(filter);
        }

        public List<PersonRole> getPersonRolesForPerson(long personId) {
                return personDao.getPersonRolesForPerson(personId);
        }

        @Override
        public String getPersonFullName(Person person) {
                return personDao.getPersonFullName(person);
        }

        @Override
        public String getPersonFullNameWithId(Person person) {
                return personDao.getPersonFullNameWithId(person);
        }

}
