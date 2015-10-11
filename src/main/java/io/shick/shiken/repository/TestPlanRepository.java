package io.shick.shiken.repository;

import io.shick.shiken.domain.TestPlan;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TestPlan entity.
 */
public interface TestPlanRepository extends JpaRepository<TestPlan,Long> {

}
