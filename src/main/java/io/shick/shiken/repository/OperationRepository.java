package io.shick.shiken.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.shick.shiken.domain.Operation;

/**
 * Spring Data JPA repository for the Keyword entity.
 */
public interface OperationRepository extends JpaRepository<Operation,String> {

}
