package info.ajanovski.eprms.spr.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import info.ajanovski.eprms.model.entities.Person;
import info.ajanovski.eprms.model.entities.Role;
import info.ajanovski.eprms.model.util.ModelConstants;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

	@Inject
	public CustomUserDetailsServiceImpl(PersonManager personManager) {
		this.personManager = personManager;
	}

	private PersonManager personManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Login attempted by: {}", username);
		Person person = personManager.getPersonByUsername(username);
		if (person != null) {
			List<String> authList = new ArrayList<String>();
			for (Role r : personManager.getRolesForPerson(person.getPersonId())) {
				if (r.getName().equals(ModelConstants.RoleAdministrator)) {
					authList.add("ROLE_ADMINISTRATOR");
				} else if (r.getName().equals(ModelConstants.RoleInstructor)) {
					authList.add("ROLE_INSTRUCTOR");
				} else if (r.getName().equals(ModelConstants.RoleStudent)) {
					authList.add("ROLE_STUDENT");
				}
			}
			logger.info("Roles by {} :{}", username, Strings.join(authList, ','));
			return new User(person.getUserName(), "", true, true, true, true,
					AuthorityUtils.commaSeparatedStringToAuthorityList(Strings.join(authList, ',')));
		} else {
			logger.error("Username {} not found! Throwing exception.", username);
			throw new UsernameNotFoundException("UserName Not Found");
		}
	}

}
