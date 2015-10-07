package io.shick.shiken.repository;

import io.shick.shiken.domain.Platform;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Platform entity.
 */
public interface PlatformRepository extends JpaRepository<Platform,Long> {

}
