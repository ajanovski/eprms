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
@Table (schema="epm_util", name="person_role")
public class PersonRole implements java.io.Serializable {
	private long personRoleId;
	private Person person;
	private Role role;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "person_role_id", unique = true, nullable = false)
	public long getPersonRoleId() {
		return this.personRoleId;
	}

	public void setPersonRoleId(long personRoleId) {
		this.personRoleId=personRoleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_person_role_person"))
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person=person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_person_role_role"))
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role=role;
	}

}
