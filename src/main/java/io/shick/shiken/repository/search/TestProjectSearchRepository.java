package io.shick.shiken.repository.search;

import io.shick.shiken.domain.TestProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TestProject entity.
 */
public interface TestProjectSearchRepository extends ElasticsearchRepository<TestProject, Long> {
}
