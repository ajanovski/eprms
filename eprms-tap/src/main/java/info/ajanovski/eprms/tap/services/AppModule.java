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

package info.ajanovski.eprms.tap.services;

import java.io.IOException;
import java.util.UUID;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.commons.OrderedConfiguration;
import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.http.services.RequestFilter;
import org.apache.tapestry5.http.services.RequestHandler;
import org.apache.tapestry5.http.services.Response;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.modules.Bootstrap4Module;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.slf4j.Logger;

import info.ajanovski.eprms.tap.data.GenericDao;
import info.ajanovski.eprms.tap.data.PersonDao;
import info.ajanovski.eprms.tap.data.ResourceDao;
import info.ajanovski.eprms.tap.util.AppConfig;

@ImportModule(Bootstrap4Module.class)
public class AppModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(AccessController.class).withId("AccessController");
		binder.bind(GenericDao.class);
		binder.bind(GenericService.class);
		binder.bind(PersonDao.class);
		binder.bind(PersonManager.class);
		binder.bind(ResourceManager.class);
		binder.bind(ResourceDao.class);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.override(SymbolConstants.APPLICATION_VERSION, "0.0.1-SNAPSHOT");
		configuration.override(SymbolConstants.PRODUCTION_MODE, false);
	}

	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,mk");
		configuration.add(SymbolConstants.HMAC_PASSPHRASE,
				AppConfig.getString("tapestry.hmac-passphrase") + UUID.randomUUID());
		configuration.add(SymbolConstants.ENABLE_HTML5_SUPPORT, true);
		configuration.add(SymbolConstants.COMPRESS_WHITESPACE, false);
	}

	@Contribute(SymbolProvider.class)
	@ApplicationDefaults
	public static void setupEnvironment(MappedConfiguration<String, Object> configuration) {
		configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
	}

	@Contribute(HibernateEntityPackageManager.class)
	public static void addHibernateEntityPackageManager(Configuration<String> configuration) {
		configuration.add("info.ajanovski.eprms.model.entities");
	}

	public RequestFilter buildTimingFilter(final Logger log) {
		return new RequestFilter() {
			public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
				long startTime = System.currentTimeMillis();
				try {
					return handler.service(request, response);
				} finally {
					long elapsed = System.currentTimeMillis() - startTime;
					log.debug("Request time: {} ms", elapsed);
				}
			}
		};
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("fontsource-fira-sans", "META-INF/resources/webjars/fontsource-fira-sans/3.0.5");
		configuration.add("tango-icon-theme", "org/freedesktop/tango");
	}

	@Contribute(RequestHandler.class)
	public void addTimingFilter(OrderedConfiguration<RequestFilter> configuration, @Local RequestFilter filter) {
		configuration.add("Timing", filter);
	}

	public static final void contributeComponentRequestHandler(
			OrderedConfiguration<ComponentRequestFilter> configuration, ComponentRequestFilter accessController,
			ApplicationStateManager asm, ComponentSource componentSource) {
		configuration.add("AccessController", accessController, "before:*");
	}

	public Logger buildLogger(final Logger logger) {
		return logger;
	}

}
