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

package info.ajanovski.eprms.spr.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.spr.data.ResourceDao;

@Service
public class ResourceManagerImpl implements ResourceManager {

	@Inject
	private ResourceDao resourceDao;

	@Override
	public List<Repository> getRepositoriesByPerson(long personId) {
		return resourceDao.getRepositoriesByPerson(personId);
	}

	@Override
	public List<Repository> getRepositoriesByTeam(long personId) {
		return resourceDao.getRepositoriesByTeam(personId);
	}

	@Override
	public List<Repository> getRepositoriesByProject(long personId) {
		return resourceDao.getRepositoriesByProject(personId);
	}

	@Override
	public List<Repository> getActiveRepositoriesByPerson(long personId) {
		return getRepositoriesByPerson(personId).stream().filter(p -> p.getDateCreated() != null)
				.collect(Collectors.toList());
	}

	@Override
	public List<Repository> getActiveRepositoriesByTeam(long personId) {
		return getRepositoriesByTeam(personId).stream().filter(p -> p.getDateCreated() != null)
				.collect(Collectors.toList());
	}

	@Override
	public List<Repository> getActiveRepositoriesByProject(long personId) {
		return getRepositoriesByProject(personId).stream().filter(p -> p.getDateCreated() != null)
				.collect(Collectors.toList());
	}

	@Override
	public List<Database> getDatabasesByProject(long personId) {
		return resourceDao.getDatabasesByProject(personId);
	}

	@Override
	public List<Database> getActiveDatabasesByProject(long personId) {
		return getDatabasesByProject(personId).stream().filter(p -> p.getDateCreated() != null)
				.collect(Collectors.toList());
	}

}
