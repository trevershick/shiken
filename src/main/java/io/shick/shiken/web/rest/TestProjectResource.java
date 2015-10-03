package io.shick.shiken.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.shick.shiken.domain.TestProject;
import io.shick.shiken.repository.TestProjectRepository;
import io.shick.shiken.repository.search.TestProjectSearchRepository;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;
import io.shick.shiken.web.rest.dto.TestProjectDTO;
import io.shick.shiken.web.rest.mapper.TestProjectMapper;
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
 * REST controller for managing TestProject.
 */
@RestController
@RequestMapping("/api")
public class TestProjectResource {

    private final Logger log = LoggerFactory.getLogger(TestProjectResource.class);

    @Inject
    private TestProjectRepository testProjectRepository;

    @Inject
    private TestProjectMapper testProjectMapper;

    @Inject
    private TestProjectSearchRepository testProjectSearchRepository;

    /**
     * POST  /testProjects -> Create a new testProject.
     */
    @RequestMapping(value = "/testProjects",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestProjectDTO> create(@Valid @RequestBody TestProjectDTO testProjectDTO) throws URISyntaxException {
        log.debug("REST request to save TestProject : {}", testProjectDTO);
        if (testProjectDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testProject cannot already have an ID").body(null);
        }
        TestProject testProject = testProjectMapper.testProjectDTOToTestProject(testProjectDTO);
        TestProject result = testProjectRepository.save(testProject);
        testProjectSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testProjects/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("testProject", result.getId().toString()))
                .body(testProjectMapper.testProjectToTestProjectDTO(result));
    }

    /**
     * PUT  /testProjects -> Updates an existing testProject.
     */
    @RequestMapping(value = "/testProjects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestProjectDTO> update(@Valid @RequestBody TestProjectDTO testProjectDTO) throws URISyntaxException {
        log.debug("REST request to update TestProject : {}", testProjectDTO);
        if (testProjectDTO.getId() == null) {
            return create(testProjectDTO);
        }
        TestProject testProject = testProjectMapper.testProjectDTOToTestProject(testProjectDTO);
        TestProject result = testProjectRepository.save(testProject);
        testProjectSearchRepository.save(testProject);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("testProject", testProjectDTO.getId().toString()))
                .body(testProjectMapper.testProjectToTestProjectDTO(result));
    }

    /**
     * GET  /testProjects -> get all the testProjects.
     */
    @RequestMapping(value = "/testProjects",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TestProjectDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<TestProject> page = testProjectRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testProjects", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(testProjectMapper::testProjectToTestProjectDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /testProjects/:id -> get the "id" testProject.
     */
    @RequestMapping(value = "/testProjects/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestProjectDTO> get(@PathVariable Long id) {
        log.debug("REST request to get TestProject : {}", id);
        return Optional.ofNullable(testProjectRepository.findOne(id))
            .map(testProjectMapper::testProjectToTestProjectDTO)
            .map(testProjectDTO -> new ResponseEntity<>(
                testProjectDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testProjects/:id -> delete the "id" testProject.
     */
    @RequestMapping(value = "/testProjects/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete TestProject : {}", id);
        testProjectRepository.delete(id);
        testProjectSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testProject", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testProjects/:query -> search for the testProject corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testProjects/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestProject> search(@PathVariable String query) {
        return StreamSupport
            .stream(testProjectSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
