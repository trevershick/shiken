package io.shick.shiken.repository;

import io.shick.shiken.domain.TestProject;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TestProject entity.
 */
public interface TestProjectRepository extends JpaRepository<TestProject,Long> {

}
