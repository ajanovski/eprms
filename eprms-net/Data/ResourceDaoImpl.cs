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

using System.Collections.Generic;
using info.ajanovski.eprms.net.Models;
using Microsoft.Extensions.Logging;
using System;
using System.Linq;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using Npgsql;

namespace info.ajanovski.eprms.net.Data {

    public class ResourceDaoImpl : ResourceDao {


        private readonly EPRMSNetDbContext session;

        private readonly ILogger<ResourceDaoImpl> logger;

        public ResourceDaoImpl(EPRMSNetDbContext session, ILogger<ResourceDaoImpl> logger) {
            this.session = session;
            this.logger = logger;
        }

        public List<Repository> getRepositoriesByPerson(long personId) {
            try {
                return session.Repositories.Where(r => r.Person.PersonId == personId).ToList();
            } catch (Exception e) {
                logger.LogError(e.ToString());
                return new List<Repository>();
            }
        }

        public List<Repository> getRepositoriesByTeam(long personId) {
            try {
                return session.Repositories.FromSqlRaw(@"
					select r from Repository r join r.team t, TeamMember tm join tm.person p
					where tm.team.teamId=t.teamId and r.person.personId=:personId
					", new NpgsqlParameter("@personId", personId)).ToList();
            } catch (Exception e) {
                logger.LogError(e.ToString());
                return new List<Repository>();
            }
        }

        public List<Repository> getRepositoriesByProject(long personId) {
            try {
                return session.Repositories.FromSqlRaw(@"
					select r from Repository r join r.project pr,
					Responsibility res join res.team t, TeamMember tm join tm.person p
					where pr.projectId=res.project.projectId and tm.team.teamId=t.teamId and
					tm.person.personId=@personId
					", new NpgsqlParameter("@personId", personId)).ToList();
            } catch (Exception e) {
                logger.LogError(e.ToString());
                return new List<Repository>();
            }
        }

        public List<Database> getDatabasesByProject(long personId) {
            try {
                return session.Databases.FromSqlRaw(@"
					select d.* from epm_main.Database d 
					join epm_main.project pr on d.project_id=pr.project_id 
					join epm_main.Responsibility res on res.project_id=pr.project_id
					join epm_main.team t on t.team_id=res.team_id
					join epm_main.Team_Member tm on tm.team_id=t.team_id
					join epm_main.person p on tm.person_id=p.person_id
					where p.person_Id=@personId
					", new NpgsqlParameter("@personId", personId)).ToList();
            } catch (Exception e) {
                logger.LogError(e.ToString());
                return null;
            }

        }
    }
}
