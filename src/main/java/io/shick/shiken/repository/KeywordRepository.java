package io.shick.shiken.repository;

import io.shick.shiken.domain.Keyword;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Keyword entity.
 */
public interface KeywordRepository extends JpaRepository<Keyword,String> {

}
