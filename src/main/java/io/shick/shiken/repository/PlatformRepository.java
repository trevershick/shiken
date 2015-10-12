package io.shick.shiken.repository;

import io.shick.shiken.domain.Platform;
import org.springframework.data.jpa.repository.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Spring Data JPA repository for the Platform entity.
 */
public interface PlatformRepository extends JpaRepository<Platform,Long> {

	@Override
	@Secured("OP_MG_PLATFORM")
	Platform save(Platform p);
	
	@Override
	@Secured("OP_MG_PLATFORM")
	void delete(Platform p);
}
