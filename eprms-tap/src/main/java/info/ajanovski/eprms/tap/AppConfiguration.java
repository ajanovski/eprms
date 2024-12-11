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

package info.ajanovski.eprms.tap;

import java.util.EnumSet;

import org.apache.tapestry5.TapestryFilter;
import org.apereo.cas.client.authentication.AuthenticationFilter;
import org.apereo.cas.client.session.SingleSignOutFilter;
import org.apereo.cas.client.session.SingleSignOutHttpSessionListener;
import org.apereo.cas.client.util.HttpServletRequestWrapperFilter;
import org.apereo.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import info.ajanovski.eprms.tap.util.AppConfig;
import info.ajanovski.eprms.tap.util.UTF8Filter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;

@Configuration
@ComponentScan({ "info.ajanovski.eprms.tap" })
public class AppConfiguration {

	@Bean
	public ServletContextInitializer initializer() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.setInitParameter("tapestry.app-package", "info.ajanovski.eprms.tap");
				servletContext.setInitParameter("tapestry.development-modules",
						"info.ajanovski.eprms.tap.services.DevelopmentModule");
				servletContext.setInitParameter("tapestry.qa-modules", "info.ajanovski.eprms.tap.services.QaModule");
                //servletContext.setInitParameter("tapestry.use-external-spring-context", "true");

				servletContext.setInitParameter("artifactParameterName", "ticket");
				servletContext.setInitParameter("casServerLogoutUrl",
						AppConfig.getString("cas.server") + "/cas/logout");
				servletContext.setInitParameter("casServerLoginUrl", AppConfig.getString("cas.server") + "/cas/login");
				servletContext.setInitParameter("casServerUrlPrefix", AppConfig.getString("cas.server") + "/cas");
				servletContext.setInitParameter("service",
						AppConfig.getString("app.server") + servletContext.getContextPath());

				servletContext.addFilter("encodingFilter", UTF8Filter.class).addMappingForUrlPatterns(
						EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false, "/*");

				servletContext.addFilter("CAS Single Sign Out Filter", SingleSignOutFilter.class)
						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false,
								"/*");
				servletContext.addFilter("CAS Authentication Filter", AuthenticationFilter.class)
						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false,
								"/*");
				servletContext.addFilter("CAS Validation Filter", Cas20ProxyReceivingTicketValidationFilter.class)
						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false,
								"/*");
				servletContext.addFilter("CAS HttpServletRequest Wrapper Filter", HttpServletRequestWrapperFilter.class)
						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false,
								"/*");

				servletContext.addFilter("app", TapestryFilter.class).addMappingForUrlPatterns(
						EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false, "/*");

				servletContext.addListener(SingleSignOutHttpSessionListener.class);

                //servletContext.addFilter("app", TapestrySpringFilter.class).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR), false, "/*");

				servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
			}
		};
	}

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return registry -> {
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error404"));
        };
    }

    //	@Bean
//	public ConfigurableServletWebServerFactory webServerFactory() {
//		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
//		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error404"));
//		return factory;
//	}

//	@Bean(name = "messageSource")
//	public MessageSource getMessageResource() {
//		ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
//		messageResource.setBasename("classpath:app");
//		messageResource.setDefaultEncoding("UTF-8");
//		return messageResource;
//	}
//
//	@Bean(name = "localeResolver")
//	public LocaleResolver getLocaleResolver() {
//		CookieLocaleResolver resolver = new CookieLocaleResolver();
//		resolver.setCookieDomain("localeCookie");
//		resolver.setCookieMaxAge(60 * 60);
//		return resolver;
//	}
}
