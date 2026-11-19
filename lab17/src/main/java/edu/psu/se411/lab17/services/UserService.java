package edu.psu.se411.lab17.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.psu.se411.lab17.exceptions.UsernameTakenException;
import edu.psu.se411.lab17.model.User;
import edu.psu.se411.lab17.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AuthenticationManager authManager;
	private final JwtService jwtService;
	private final PasswordEncoder encoder;

	public UserService(UserRepository userRepository, AuthenticationManager authManager,
			JwtService jwtService, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.encoder = encoder;
	}

	/** User is an immutable record, so a new one is built with the hashed password. */
	public User register(User user) {
		if (userRepository.existsByUsername(user.username())) {
			throw new UsernameTakenException(user.username());
		}
		try {
			return userRepository.save(new User(null, user.username(), encoder.encode(user.password())));
		} catch (DuplicateKeyException e) {
			throw new UsernameTakenException(user.username());
		}
	}

	/** Returns a JWT; throws AuthenticationException if the credentials are wrong. */
	public String verify(User user) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.username(), user.password()));
		return jwtService.generateToken(auth.getName());
	}
}
