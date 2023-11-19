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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.beanvalidator.BeanValidatorConfigurer;
import org.apache.tapestry5.beanvalidator.BeanValidatorSource;
import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.commons.OrderedConfiguration;
import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import org.apache.tapestry5.hibernate.HibernateSymbols;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.http.services.RequestGlobals;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.modules.Bootstrap4Module;
import org.apache.tapestry5.services.ApplicationStateContribution;
import org.apache.tapestry5.services.ApplicationStateCreator;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PersistentLocale;
import org.hibernate.Session;
import org.slf4j.Logger;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.PersonRole;
import info.ajanovski.eprms.model.util.ModelConstants;
import info.ajanovski.eprms.mq.MessagingService;
import info.ajanovski.eprms.tap.data.DiscussionDao;
import info.ajanovski.eprms.tap.data.GenericDao;
import info.ajanovski.eprms.tap.data.PersonDao;
import info.ajanovski.eprms.tap.data.ProjectDao;
import info.ajanovski.eprms.tap.data.ResourceDao;
import info.ajanovski.eprms.tap.data.TranslationDao;
import info.ajanovski.eprms.tap.util.AppConfig;
import info.ajanovski.eprms.tap.util.UserInfo;
import info.ajanovski.eprms.tap.util.UserInfo.UserRole;

@ImportModule(Bootstrap4Module.class)
public class AppModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(AccessControllerRequestFilter.class);
		binder.bind(CourseManager.class);
		binder.bind(GenericDao.class);
		binder.bind(GenericService.class);
		binder.bind(PersonDao.class);
		binder.bind(PersonManager.class);
		binder.bind(ProjectDao.class);
		binder.bind(ProjectManager.class);
		binder.bind(ResourceManager.class);
		binder.bind(ResourceDao.class);
		binder.bind(MessagingService.class);
		binder.bind(TranslationDao.class);
		binder.bind(TranslationService.class);
		binder.bind(SystemConfigService.class);
		binder.bind(DiscussionManager.class);
		binder.bind(DiscussionDao.class);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.override(SymbolConstants.APPLICATION_VERSION, "0.0.4-SNAPSHOT");
		configuration.override(SymbolConstants.PRODUCTION_MODE, false);
	}

	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "mk,en");
		configuration.add(SymbolConstants.HMAC_PASSPHRASE,
				AppConfig.getString("tapestry.hmac-passphrase") + UUID.randomUUID());
		configuration.add(SymbolConstants.ENABLE_HTML5_SUPPORT, true);
		configuration.add(SymbolConstants.COMPRESS_WHITESPACE, false);
		configuration.add(SymbolConstants.CHARSET, "UTF-8");

		configuration.add(HibernateSymbols.EARLY_START_UP, true);

		configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
	}

	@Contribute(HibernateEntityPackageManager.class)
	public static void addHibernateEntityPackageManager(Configuration<String> configuration) {
		configuration.add("info.ajanovski.eprms.model.entities");
	}

	@Match({ "*Service", "*Dao", "*Manager" })
	public static void adviseEnableTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}

	@Decorate(serviceInterface = ThreadLocale.class)
	public ThreadLocale decorateThreadLocale(final ThreadLocale threadLocale, final PersistentLocale persistentLocale) {
		return new ThreadLocale() {
			@Override
			public void setLocale(Locale locale) {
				threadLocale.setLocale(locale);
			}

			@Override
			public Locale getLocale() {
				if (!persistentLocale.isSet()) {
					setLocale(new Locale("en"));
					persistentLocale.set(new Locale("en"));
				}
				return threadLocale.getLocale();
			}

		};
	}

	public static final void contributeComponentRequestHandler(
			OrderedConfiguration<ComponentRequestFilter> configuration,
			ComponentRequestFilter accessControllerRequestFilter, ApplicationStateManager asm,
			ComponentSource componentSource) {
		configuration.add("AccessControllerRequestFilter", accessControllerRequestFilter, "before:*");
	}

	public void contributeApplicationStateManager(
			MappedConfiguration<Class, ApplicationStateContribution> configuration, Session session,
			PersonManager personManager, RequestGlobals requestGlobals, Logger logger) {
		ApplicationStateCreator<UserInfo> userInfoCreator = new ApplicationStateCreator<UserInfo>() {
			public UserInfo create() {
				logger.debug("userInfoCreator.create entered");

				UserInfo userInfo = new UserInfo();
				userInfo.setUserRoles(null);
				userInfo.setPersonId(null);
				userInfo.setUserName(null);

				try {
					String userName = requestGlobals.getHTTPServletRequest().getRemoteUser();
					userInfo.setUserName(userName);
					logger.info("Login by user: " + userName);

					Person loggedInPerson = (Person) session.getSession()
							.createQuery("from Person p where active=true and userName=:userName")
							.setParameter("userName", userName).getSingleResult();

					if (loggedInPerson == null) {
						userInfo.setUserRoles(null);
						userInfo.setPersonId(null);
					} else {
						logger.debug("Login personId: {}", loggedInPerson.getPersonId());

						List<UserInfo.UserRole> userRoles = new ArrayList<UserRole>();

						for (PersonRole pr : personManager.getPersonRolesForPerson(loggedInPerson.getPersonId())) {
							if (pr.getRole().getName().equals(ModelConstants.RoleAdministrator)) {
								userRoles.add(UserRole.ADMINISTRATOR);
							} else if (pr.getRole().getName().equals(ModelConstants.RoleInstructor)) {
								userRoles.add(UserRole.INSTRUCTOR);
							} else if (pr.getRole().getName().equals(ModelConstants.RoleStudent)) {
								userRoles.add(UserRole.STUDENT);
							}
						}

						if (userRoles.size() == 0) {
							logger.debug("Login user role is set to NONE");
							userRoles.add(UserRole.NONE);
						}

						logger.debug("Login user has {} roles", userRoles.size());

						userInfo.setUserName(userName);
						userInfo.setPersonId(loggedInPerson.getPersonId());
						userInfo.setUserRoles(userRoles);
						logger.debug("userInfo is now initialized");

					}

					return userInfo;

				} catch (Exception e) {
					if (userInfo.getUserName() != null) {
						logger.error("userName {} is not found", userInfo.getUserName());
					} else {
						logger.error("userName is empty");
					}
					// throw new NoSuchUserException();
					return userInfo;
				}
			}
		};
		configuration.add(UserInfo.class, new ApplicationStateContribution("session", userInfoCreator));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("fontsource-fira-sans", "META-INF/resources/webjars/fontsource-fira-sans/3.0.5");
		configuration.add("ck", "META-INF/modules/vendor");
		// configuration.add("tango-icon-theme", "org/freedesktop/tango");
	}

	@Contribute(BeanValidatorSource.class)
	public static void provideBeanValidatorConfigurer(OrderedConfiguration<BeanValidatorConfigurer> configuration) {
		configuration.add("noXMLBeanValidatorConfigurer", new BeanValidatorConfigurer() {
			public void configure(javax.validation.Configuration<?> configuration) {
				configuration.ignoreXmlConfiguration();
			}
		});
	}

	public Logger buildLogger(final Logger logger) {
		return logger;
	}

}
