package io.shick.shiken.repository.search;

import io.shick.shiken.domain.Platform;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Platform entity.
 */
public interface PlatformSearchRepository extends ElasticsearchRepository<Platform, Long> {
}
