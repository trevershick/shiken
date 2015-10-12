package io.shick.shiken.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.shick.shiken.domain.Role;

/**
 * Spring Data JPA repository for the Keyword entity.
 */
public interface RoleRepository extends JpaRepository<Role,String> {

}
