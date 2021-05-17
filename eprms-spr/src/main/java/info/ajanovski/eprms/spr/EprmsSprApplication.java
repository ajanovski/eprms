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

package info.ajanovski.eprms.spr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "info.ajanovski.eprms.model.entities")
@EnableTransactionManagement
public class EprmsSprApplication {

	private static final Logger logger = LoggerFactory.getLogger(EprmsSprApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EprmsSprApplication.class, args);
	}

	@Bean(name = "studentPageNames")
	public String[] getStudentPageNames() {
		return new String[] { "MyDatabases", "MyRepositories", "MyRepositoryAuth", "admin/ManageDatabases" };
	}

	@Bean(name = "adminPageNames")
	public String[] getAdminPageNames() {
		return new String[] { "admin/Projects", "admin/Teams", "admin/ManageDatabases", "admin/ManageRepositories" };
	}

}
