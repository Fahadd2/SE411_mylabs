package edu.psu.se411.lab17.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.psu.se411.lab17.model.User;
import edu.psu.se411.lab17.services.UserService;

/** HTTP only: uses UserService, never a repository. */
@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/** POST /register : 200 with the new user (password emptied), 409 if the username is taken */
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		User saved = userService.register(user);
		return ResponseEntity.ok(new User(saved.id(), saved.username(), "")); // never return the hash
	}

	/** POST /login : 200 with a JWT, or 401 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.verify(user));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}
}
