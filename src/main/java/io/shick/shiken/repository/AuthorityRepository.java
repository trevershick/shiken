package io.shick.shiken.repository;

import io.shick.shiken.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Role, String> {
}
