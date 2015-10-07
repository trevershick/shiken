package io.shick.shiken.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.shick.shiken.domain.Platform;
import io.shick.shiken.repository.PlatformRepository;
import io.shick.shiken.repository.search.PlatformSearchRepository;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;
import io.shick.shiken.web.rest.dto.PlatformDTO;
import io.shick.shiken.web.rest.mapper.PlatformMapper;
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
 * REST controller for managing Platform.
 */
@RestController
@RequestMapping("/api")
public class PlatformResource {

    private final Logger log = LoggerFactory.getLogger(PlatformResource.class);

    @Inject
    private PlatformRepository platformRepository;

    @Inject
    private PlatformMapper platformMapper;

    @Inject
    private PlatformSearchRepository platformSearchRepository;

    /**
     * POST  /platforms -> Create a new platform.
     */
    @RequestMapping(value = "/platforms",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlatformDTO> create(@Valid @RequestBody PlatformDTO platformDTO) throws URISyntaxException {
        log.debug("REST request to save Platform : {}", platformDTO);
        if (platformDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new platform cannot already have an ID").body(null);
        }
        Platform platform = platformMapper.platformDTOToPlatform(platformDTO);
        Platform result = platformRepository.save(platform);
        platformSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/platforms/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("platform", result.getId().toString()))
                .body(platformMapper.platformToPlatformDTO(result));
    }

    /**
     * PUT  /platforms -> Updates an existing platform.
     */
    @RequestMapping(value = "/platforms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlatformDTO> update(@Valid @RequestBody PlatformDTO platformDTO) throws URISyntaxException {
        log.debug("REST request to update Platform : {}", platformDTO);
        if (platformDTO.getId() == null) {
            return create(platformDTO);
        }
        Platform platform = platformMapper.platformDTOToPlatform(platformDTO);
        Platform result = platformRepository.save(platform);
        platformSearchRepository.save(platform);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("platform", platformDTO.getId().toString()))
                .body(platformMapper.platformToPlatformDTO(result));
    }

    /**
     * GET  /platforms -> get all the platforms.
     */
    @RequestMapping(value = "/platforms",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PlatformDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Platform> page = platformRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/platforms", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(platformMapper::platformToPlatformDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /platforms/:id -> get the "id" platform.
     */
    @RequestMapping(value = "/platforms/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlatformDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Platform : {}", id);
        return Optional.ofNullable(platformRepository.findOne(id))
            .map(platformMapper::platformToPlatformDTO)
            .map(platformDTO -> new ResponseEntity<>(
                platformDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /platforms/:id -> delete the "id" platform.
     */
    @RequestMapping(value = "/platforms/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Platform : {}", id);
        platformRepository.delete(id);
        platformSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("platform", id.toString())).build();
    }

    /**
     * SEARCH  /_search/platforms/:query -> search for the platform corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/platforms/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Platform> search(@PathVariable String query) {
        return StreamSupport
            .stream(platformSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
