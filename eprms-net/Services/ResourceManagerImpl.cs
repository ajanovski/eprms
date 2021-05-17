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
    public class ResourceManagerImpl : ResourceManager {

        private ResourceDao repositoryDao;

        public ResourceManagerImpl(ResourceDao repositoryDao) {
            this.repositoryDao = repositoryDao;
        }

        public List<Repository> getRepositoriesByPerson(long personId) {
            return repositoryDao.getRepositoriesByPerson(personId);
        }

        public List<Repository> getRepositoriesByTeam(long personId) {
            return repositoryDao.getRepositoriesByTeam(personId);
        }

        public List<Repository> getRepositoriesByProject(long personId) {
            return repositoryDao.getRepositoriesByProject(personId);
        }

        public List<Repository> getActiveRepositoriesByPerson(long personId) {
            return getRepositoriesByPerson(personId).FindAll(p => p.DateCreated != null);
        }

        public List<Repository> getActiveRepositoriesByTeam(long personId) {
            return getRepositoriesByTeam(personId).FindAll(p => p.DateCreated != null);
        }

        public List<Repository> getActiveRepositoriesByProject(long personId) {
            return getRepositoriesByProject(personId).FindAll(p => p.DateCreated != null);
        }

        public List<Database> getDatabasesByProject(long personId) {
            return repositoryDao.getDatabasesByProject(personId);
        }

        public List<Database> getActiveDatabasesByProject(long personId) {
            return getDatabasesByProject(personId);
        }

    }
}