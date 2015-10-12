package io.shick.shiken.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.shick.shiken.domain.TestPlan;
import io.shick.shiken.repository.TestPlanRepository;
import io.shick.shiken.repository.search.TestPlanSearchRepository;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;
import io.shick.shiken.web.rest.dto.TestPlanDTO;
import io.shick.shiken.web.rest.mapper.TestPlanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TestPlan.
 */
@RestController
@RequestMapping("/api")
public class TestPlanResource {

    private final Logger log = LoggerFactory.getLogger(TestPlanResource.class);

    @Inject
    private TestPlanRepository testPlanRepository;

    @Inject
    private TestPlanMapper testPlanMapper;

    @Inject
    private TestPlanSearchRepository testPlanSearchRepository;

    /**
     * POST  /testPlans -> Create a new testPlan.
     */
    @RequestMapping(value = "/testPlans",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestPlanDTO> create(@Valid @RequestBody TestPlanDTO testPlanDTO) throws URISyntaxException {
        log.debug("REST request to save TestPlan : {}", testPlanDTO);
        if (testPlanDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testPlan cannot already have an ID").body(null);
        }
        TestPlan testPlan = testPlanMapper.testPlanDTOToTestPlan(testPlanDTO);
        TestPlan result = testPlanRepository.save(testPlan);
        testPlanSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testPlans/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("testPlan", result.getId().toString()))
                .body(testPlanMapper.testPlanToTestPlanDTO(result));
    }

    /**
     * PUT  /testPlans -> Updates an existing testPlan.
     */
    @RequestMapping(value = "/testPlans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestPlanDTO> update(@Valid @RequestBody TestPlanDTO testPlanDTO) throws URISyntaxException {
        log.debug("REST request to update TestPlan : {}", testPlanDTO);
        if (testPlanDTO.getId() == null) {
            return create(testPlanDTO);
        }
        TestPlan testPlan = testPlanMapper.testPlanDTOToTestPlan(testPlanDTO);
        TestPlan result = testPlanRepository.save(testPlan);
        testPlanSearchRepository.save(testPlan);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("testPlan", testPlanDTO.getId().toString()))
                .body(testPlanMapper.testPlanToTestPlanDTO(result));
    }

    /**
     * GET  /testPlans -> get all the testPlans.
     */
    @RequestMapping(value = "/testPlans",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TestPlanDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<TestPlan> page = testPlanRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testPlans", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(testPlanMapper::testPlanToTestPlanDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /testPlans/:id -> get the "id" testPlan.
     */
    @RequestMapping(value = "/testPlans/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestPlanDTO> get(@PathVariable Long id) {
        log.debug("REST request to get TestPlan : {}", id);
        return Optional.ofNullable(testPlanRepository.findOne(id))
            .map(testPlanMapper::testPlanToTestPlanDTO)
            .map(testPlanDTO -> new ResponseEntity<>(
                testPlanDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testPlans/:id -> delete the "id" testPlan.
     */
    @RequestMapping(value = "/testPlans/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	// TODO return the entity upon delete
    	
        log.debug("REST request to delete TestPlan : {}", id);
        testPlanRepository.delete(id);
        testPlanSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testPlan", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testPlans/:query -> search for the testPlan corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testPlans/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestPlan> search(@PathVariable String query) {
        return StreamSupport
            .stream(testPlanSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
