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

using System;
using System.Collections.Generic;
using info.ajanovski.eprms.net.Models;
using Microsoft.Extensions.Logging;
using System.Linq;

namespace info.ajanovski.eprms.net.Data {
    public class PersonDaoImpl : PersonDao {

        private readonly EPRMSNetDbContext session;

        private readonly ILogger<PersonDaoImpl> logger;

        public PersonDaoImpl(EPRMSNetDbContext session, ILogger<PersonDaoImpl> logger) {
            this.session = session;
            this.logger = logger;
        }

        public List<Person> getAllPersons() {
            try {
                return session.People.OrderBy(p => p.LastName).ToList();
            } catch (Exception e) {
                logger.LogError("Exception occurs : {} on complete getAllPersons()." + e);
                return null;
            }
        }

        public Person getPersonByUsername(string username) {
            return session.People.Where(p => p.UserName == username).FirstOrDefault();
        }

        public List<Person> getPersonByFilter(string filter) {
            // String f = "%" + filter.toLowerCase() + "%";
            // 		.createQuery("select p from Person p  where (lower(concat(userName,firstName,lastName)) like :filter)");
            return session.People.Where(p => p.UserName.Contains(filter.ToLower())).OrderBy(p => p.LastName).ToList();
        }

        public List<PersonRole> getPersonRolesForPerson(long personId) {
            return session.PersonRoles.Where(pr => pr.Person.PersonId == personId).ToList();
        }

        public string getPersonFullName(Person person) {
            return person.LastName + " " + person.FirstName;
        }

        public string getPersonFullNameWithId(Person person) {
            return person.LastName + " " + person.FirstName + " [" + person.UserName + "]";
        }

        public Person getPersonByEmail(string email) {
            return session.People.Where(p => p.Email.Equals(email)).FirstOrDefault();
        }
    }
}
