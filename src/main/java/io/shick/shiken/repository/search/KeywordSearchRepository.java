package io.shick.shiken.repository.search;

import io.shick.shiken.domain.Keyword;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Keyword entity.
 */
public interface KeywordSearchRepository extends ElasticsearchRepository<Keyword, String> {
}
