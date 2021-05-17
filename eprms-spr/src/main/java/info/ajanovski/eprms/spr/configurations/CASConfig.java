package info.ajanovski.eprms.spr.configurations;

import java.util.Arrays;

import javax.servlet.http.HttpSessionEvent;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import info.ajanovski.eprms.spr.services.CustomUserDetailsService;
import info.ajanovski.eprms.spr.util.AppConfig;

@Configuration
public class CASConfig {

	private final CustomUserDetailsService customUserDetailsService;

	@Autowired
	public CASConfig(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(AppConfig.getString("app.server") + "/login/cas");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint(ServiceProperties serviceProperties) {
		CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
		entryPoint.setLoginUrl(AppConfig.getString("cas.server") + "/cas/login");
		entryPoint.setServiceProperties(serviceProperties);
		return entryPoint;
	}

	@Bean
	public TicketValidator ticketValidator() {
		return new Cas20ServiceTicketValidator(AppConfig.getString("cas.server") + "/cas");
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(this.serviceProperties());
		provider.setTicketValidator(this.ticketValidator());
		provider.setUserDetailsService(this.customUserDetailsService);
		provider.setKey("CAS_PROVIDER_LOCALHOST_8080");
		return provider;
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties serviceProperties) throws Exception {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setServiceProperties(serviceProperties);
		filter.setAuthenticationManager(new ProviderManager(Arrays.asList(this.casAuthenticationProvider())));
		return filter;
	}

	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

	@Bean
	public LogoutFilter logoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(AppConfig.getString("cas.server") + "/cas/logout",
				this.securityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl(AppConfig.getString("app.server") + "/logout/cas");
		return logoutFilter;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setLogoutCallbackPath(AppConfig.getString("cas.server") + "/cas");
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}

	@EventListener
	public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(HttpSessionEvent event) {
		return new SingleSignOutHttpSessionListener();
	}

}