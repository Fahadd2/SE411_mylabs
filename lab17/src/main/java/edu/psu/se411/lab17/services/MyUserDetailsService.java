package edu.psu.se411.lab17.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.psu.se411.lab17.model.User;
import edu.psu.se411.lab17.model.UserPrincipal;
import edu.psu.se411.lab17.repositories.UserRepository;

/**
 * Loads users from MySQL for Spring Security (the database version of
 * InMemoryUserDetailsManager). It only finds the user: checking the password
 * is the AuthenticationProvider's job, through the PasswordEncoder.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public MyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		return new UserPrincipal(user);
	}
}
