package edu.psu.se411.lab17.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.psu.se411.lab17.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	/** Spring Data derives SELECT ... WHERE username = ? from the method name. */
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
