package io.shick.shiken.repository.search;

import io.shick.shiken.domain.TestPlan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TestPlan entity.
 */
public interface TestPlanSearchRepository extends ElasticsearchRepository<TestPlan, Long> {
}
