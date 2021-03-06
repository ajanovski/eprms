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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Repository;

public class ResourceDaoImpl implements ResourceDao {

	@Inject
	private Session session;

	private Session getEntityManager() {
		return session.getSession();
	}

	@Override
	public List<Repository> getRepositoriesByPerson(long personId) {
		try {
			return getEntityManager().createQuery("from Repository r where r.person.personId=:personId")
					.setParameter("personId", personId).getResultList();
		} catch (Exception e) {
			return new ArrayList<Repository>();
		}
	}

	@Override
	public List<Repository> getRepositoriesByTeam(long personId) {
		try {
			return getEntityManager().createQuery("""
					select r from Repository r join r.team t, TeamMember tm join tm.person p
					where tm.team.teamId=t.teamId and r.person.personId=:personId
					""").setParameter("personId", personId).getResultList();
		} catch (Exception e) {
			return new ArrayList<Repository>();
		}
	}

	@Override
	public List<Repository> getRepositoriesByProject(long personId) {
		try {
			return getEntityManager().createQuery("""
					select r from Repository r join r.project pr,
					Responsibility res join res.team t, TeamMember tm join tm.person p
					where pr.projectId=res.project.projectId and tm.team.teamId=t.teamId and
					tm.person.personId=:personId
					""").setParameter("personId", personId).getResultList();
		} catch (Exception e) {
			return new ArrayList<Repository>();
		}
	}

	@Override
	public List<Database> getDatabasesByProject(long personId) {
		try {
			return getEntityManager().createQuery("""
					select d from Database d join d.project pr,
					Responsibility res join res.team t, TeamMember tm join tm.person p
					where pr.projectId=res.project.projectId and tm.team.teamId=t.teamId and
					tm.person.personId=:personId
					""").setParameter("personId", personId).getResultList();
		} catch (Exception e) {
			return new ArrayList<Database>();
		}

	}
}
