/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource Management 
 * System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *     
 * EPRMS is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *     
 * You should have received a copy of the GNU General Public License along 
 * with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 * 
 ******************************************************************************/

package info.ajanovski.eprms.model.entities;

import java.util.*;
import javax.persistence.*;

/*
*/
@Entity
@Table (schema="epm_util", name="role")
public class Role implements java.io.Serializable {
	private long roleId;
	private String name;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "role_id", unique = true, nullable = false)
	public long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId=roleId;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name=name;
	}

}
