package it.tecnosphera.booking.classroom.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tecnosphera.booking.classroom.model.Role;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepositoryInterface userDao;

	public void setUserDao(UserRepositoryInterface userDao) {
		this.userDao = userDao;
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		User user = userDao.findByEmail(username + "@tecnosphera.it");
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());

		return buildUserForAuthentication(user, authorities);

	}

	private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user,
			List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.isEnabled(), true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(List<Role> roles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (Role role : roles) {
			setAuths.add(new SimpleGrantedAuthority(role.getName()));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}
}
