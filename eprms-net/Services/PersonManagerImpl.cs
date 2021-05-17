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

using System.Collections.Generic;
using info.ajanovski.eprms.net.Models;
using info.ajanovski.eprms.net.Data;

namespace info.ajanovski.eprms.net.Services {

    public class PersonManagerImpl : PersonManager {

        private readonly PersonDao personDao;

        public PersonManagerImpl(PersonDao personDao) {
            this.personDao = personDao;
        }


        public List<Person> getAllPersons() {
            return this.personDao.getAllPersons();
        }

        public Person getPersonByUsername(string username) {
            return this.personDao.getPersonByUsername(username);
        }

        // public Person getPersonById(long personId) {
        //         return genericService.getByPK(Person.class, personId);
        // }

        public List<Person> getPersonByFilter(string filter) {
            return personDao.getPersonByFilter(filter);
        }

        public List<PersonRole> getPersonRolesForPerson(long personId) {
            return personDao.getPersonRolesForPerson(personId);
        }

        public string getPersonFullName(Person person) {
            return personDao.getPersonFullName(person);
        }

        public string getPersonFullNameWithId(Person person) {
            return personDao.getPersonFullNameWithId(person);
        }

        public Person getPersonByEmail(string email) {
            return personDao.getPersonByEmail(email);
        }

    }
}