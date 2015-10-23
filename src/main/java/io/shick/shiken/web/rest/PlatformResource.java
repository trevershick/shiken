package io.shick.shiken.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.shick.shiken.domain.Platform;
import io.shick.shiken.repository.PlatformRepository;
import io.shick.shiken.web.rest.dto.PlatformDTO;
import io.shick.shiken.web.rest.mapper.PlatformMapper;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;

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

    /**
     * POST  /platforms -> Create a new platform.
     */
    @RequestMapping(value = "/platforms",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlatformDTO> create(@Valid @RequestBody PlatformDTO platformDTO) throws URISyntaxException {
        log.debug("REST request to save Platform : {}", platformDTO);
        Platform platform = platformMapper.platformDTOToPlatform(platformDTO);
        Platform result = platformRepository.save(platform);
        return ResponseEntity.created(new URI("/api/platforms/" + result.getName()))
                .headers(HeaderUtil.createEntityCreationAlert("platform", result.getName().toString()))
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
        if (platformDTO.getName() == null) {
            return create(platformDTO);
        }
        Platform platform = platformMapper.platformDTOToPlatform(platformDTO);
        Platform result = platformRepository.save(platform);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("platform", platformDTO.getName().toString()))
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
    public ResponseEntity<PlatformDTO> get(@PathVariable String id) {
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
    public ResponseEntity<PlatformDTO> delete(@PathVariable String id) {
        log.debug("REST request to delete Platform : {}", id);
        
        return Optional.ofNullable(platformRepository.findOne(id))
        			.map(p -> { platformRepository.delete(id); return p; })
        			.map(platformMapper::platformToPlatformDTO)
                    .map(dto -> ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("platform", id.toString())).body(dto))
                    .orElse(new ResponseEntity<PlatformDTO>(HttpStatus.NOT_FOUND));
    }

}
