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

package info.ajanovski.eprms.tap.entities;

import java.util.*;
import javax.persistence.*;

/*
*/
@Entity
@Table(schema = "", name = "database")
public class Database implements java.io.Serializable {
	private long databaseId;
	private String type;
	private String server;
	private String name;
	private String owner;
	private String password;
	private String tunnelUser;
	private String tunnelPassword;
	private String port;
	private String tunnelServer;
	private Project project;
	private Date dateCreated;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "database_id", unique = true, nullable = false)
	public long getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "server")
	public String getServer() {
		return this.server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "owner")
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "tunnel_user")
	public String getTunnelUser() {
		return this.tunnelUser;
	}

	public void setTunnelUser(String tunnelUser) {
		this.tunnelUser = tunnelUser;
	}

	@Column(name = "tunnel_password")
	public String getTunnelPassword() {
		return this.tunnelPassword;
	}

	public void setTunnelPassword(String tunnelPassword) {
		this.tunnelPassword = tunnelPassword;
	}

	@Column(name = "port")
	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "tunnel_server")
	public String getTunnelServer() {
		return this.tunnelServer;
	}

	public void setTunnelServer(String tunnelServer) {
		this.tunnelServer = tunnelServer;
	}

	@Column(name = "date_created")
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date created) {
		this.dateCreated = created;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_database_Project"))
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
