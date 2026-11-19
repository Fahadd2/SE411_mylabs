package edu.psu.se411.lab17.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A user stored in MySQL. The password is always a BCrypt hash, never plain text.
 * @Table("users") is needed, otherwise Spring Data JDBC would look for a table named "user".
 */
@Table("users")
public record User(@Id Integer id, String username, String password) {
}
