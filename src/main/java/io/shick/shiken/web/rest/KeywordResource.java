package io.shick.shiken.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.shick.shiken.domain.Keyword;
import io.shick.shiken.repository.KeywordRepository;
import io.shick.shiken.repository.search.KeywordSearchRepository;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;
import io.shick.shiken.web.rest.dto.KeywordDTO;
import io.shick.shiken.web.rest.mapper.KeywordMapper;
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
 * REST controller for managing Keyword.
 */
@RestController
@RequestMapping("/api")
public class KeywordResource {

    private final Logger log = LoggerFactory.getLogger(KeywordResource.class);

    @Inject
    private KeywordRepository keywordRepository;

    @Inject
    private KeywordMapper keywordMapper;

    @Inject
    private KeywordSearchRepository keywordSearchRepository;

    /**
     * POST  /keywords -> Create a new keyword.
     */
    @RequestMapping(value = "/keywords",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeywordDTO> create(@Valid @RequestBody KeywordDTO keywordDTO) throws URISyntaxException {
        log.debug("REST request to save Keyword : {}", keywordDTO);
        // if (keywordDTO.getName() != null) {
        //     return ResponseEntity.badRequest().header("Failure", "A new keyword cannot already have an ID").body(null);
        // }
        Keyword keyword = keywordMapper.keywordDTOToKeyword(keywordDTO);
        Keyword result = keywordRepository.save(keyword);
        keywordSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/keywords/" + result.getName()))
                .headers(HeaderUtil.createEntityCreationAlert("keyword", result.getName().toString()))
                .body(keywordMapper.keywordToKeywordDTO(result));
    }

    /**
     * PUT  /keywords -> Updates an existing keyword.
     */
    @RequestMapping(value = "/keywords",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeywordDTO> update(@Valid @RequestBody KeywordDTO keywordDTO) throws URISyntaxException {
        log.debug("REST request to update Keyword : {}", keywordDTO);
        if (keywordDTO.getName() == null) {
            return create(keywordDTO);
        }
        Keyword keyword = keywordMapper.keywordDTOToKeyword(keywordDTO);
        Keyword result = keywordRepository.save(keyword);
        keywordSearchRepository.save(keyword);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("keyword", keywordDTO.getName().toString()))
                .body(keywordMapper.keywordToKeywordDTO(result));
    }

    /**
     * GET  /keywords -> get all the keywords.
     */
    @RequestMapping(value = "/keywords",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<KeywordDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Keyword> page = keywordRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/keywords", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(keywordMapper::keywordToKeywordDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /keywords/:id -> get the "id" keyword.
     */
    @RequestMapping(value = "/keywords/{name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeywordDTO> get(@PathVariable String name) {
        log.debug("REST request to get Keyword : {}", name);
        return Optional.ofNullable(keywordRepository.findOne(name))
            .map(keywordMapper::keywordToKeywordDTO)
            .map(keywordDTO -> new ResponseEntity<>(
                keywordDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /keywords/:name -> delete the "id" keyword.
     */
    @RequestMapping(value = "/keywords/{name}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String name) {
    	// TODO return the entity upon delete
        log.debug("REST request to delete Keyword : {}", name);
        keywordRepository.delete(name);
        keywordSearchRepository.delete(name);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("keyword", name.toString())).build();
    }

    /**
     * SEARCH  /_search/keywords/:query -> search for the keyword corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/keywords/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Keyword> search(@PathVariable String query) {
        return StreamSupport
            .stream(keywordSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
