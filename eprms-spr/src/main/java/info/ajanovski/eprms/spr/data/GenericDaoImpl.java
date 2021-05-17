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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenericDaoImpl implements GenericDao {

	private final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Object getByPK(Class<?> classToLoad, long id) {
		return entityManager.find(classToLoad, id);
	}

	@Override
	public void deleteByPK(Class<?> classToLoad, long id) {
		entityManager.remove(getByPK(classToLoad, id));
	}

	@Override
	public void delete(Object object) {
		entityManager.remove(object);
	}

	@Override
	public List<Object> getQueryResult(String guery) {
		try {
			Query q = entityManager.createQuery(guery);
			List<Object> l = new ArrayList<Object>();

			for (Iterator<?> it = q.getResultList().iterator(); it.hasNext();) {
				Object[] row = (Object[]) it.next();
				for (int i = 0; i < row.length; i++) {
					l.add(row[i]);
				}
				l.add(" | ");
			}

			return l;

		} catch (DataException e) {
			// Critical errors : database unreachable, etc.
			logger.error("Exception - DataAccessException occurs : {} on complete getQueryResult().", e.getMessage());
			return null;
		}
	}

	@Override
	public void saveOrUpdate(Object object) {
		entityManager.persist(object);
	}

	@Override
	public Object save(Object object) {
		// Object a = session.merge(object);
		entityManager.persist(object);
		return object;
	}

	@Override
	public List<?> getAll(Class<?> classToLoad) {
		return entityManager.createQuery("from " + classToLoad.getName()).getResultList();
	}

	@Override
	public Object getByCode(Class<?> classToLoad, String code) {
		if (code != null) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery criteriaQuery = cb.createQuery(classToLoad.getClass());
			Root root = criteriaQuery.from(classToLoad.getClass());
			criteriaQuery.select(root).where(cb.equal(root.get("code"), code));
			Query query = entityManager.createQuery(criteriaQuery);
			List results = query.getResultList();
			if (results.size() > 0) {
				return results.get(0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public List<?> getByTitleSubstring(Class<?> classToSearch, String searchSubString) {
		if (searchSubString != null) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery criteriaQuery = cb.createQuery(classToSearch.getClass());
			Root root = criteriaQuery.from(classToSearch.getClass());
			criteriaQuery.select(root).where(cb.like(root.get("title"), "%" + searchSubString + "%"));
			Query query = entityManager.createQuery(criteriaQuery);
			List results = query.getResultList();
			return results;
		} else {
			return null;
		}
	}
}
