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

import io.shick.shiken.domain.Operation;
import io.shick.shiken.repository.OperationRepository;
import io.shick.shiken.web.rest.dto.OperationDTO;
import io.shick.shiken.web.rest.mapper.OperationMapper;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Operation.
 */
@RestController
@RequestMapping("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    @Inject
    private OperationRepository operationRepository;

    @Inject
    private OperationMapper operationMapper;

    /**
     * POST  /operations -> Create a new operation.
     */
    @RequestMapping(value = "/operations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OperationDTO> create(@Valid @RequestBody OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to save Operation : {}", operationDTO);
        // if (operationDTO.getName() != null) {
        //     return ResponseEntity.badRequest().header("Failure", "A new operation cannot already have an ID").body(null);
        // }
        Operation operation = operationMapper.operationDtoToOperation(operationDTO);
        Operation result = operationRepository.save(operation);
        return ResponseEntity.created(new URI("/api/operations/" + result.getName()))
                .headers(HeaderUtil.createEntityCreationAlert("operation", result.getName().toString()))
                .body(operationMapper.operationToOperationDto(result));
    }

    /**
     * PUT  /operations -> Updates an existing operation.
     */
    @RequestMapping(value = "/operations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OperationDTO> update(@Valid @RequestBody OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to update Operation : {}", operationDTO);
        if (operationDTO.getName() == null) {
            return create(operationDTO);
        }
        Operation operation = operationMapper.operationDtoToOperation(operationDTO);
        Operation result = operationRepository.save(operation);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("operation", operationDTO.getName().toString()))
                .body(operationMapper.operationToOperationDto(result));
    }

    /**
     * GET  /operations -> get all the operations.
     */
    @RequestMapping(value = "/operations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OperationDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Operation> page = operationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operations", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(operationMapper::operationToOperationDto)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /operations/:id -> get the "id" operation.
     */
    @RequestMapping(value = "/operations/{name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OperationDTO> get(@PathVariable String name) {
        log.debug("REST request to get Operation : {}", name);
        return Optional.ofNullable(operationRepository.findOne(name))
            .map(operationMapper::operationToOperationDto)
            .map(operationDTO -> new ResponseEntity<>(
                operationDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /operations/:name -> delete the "id" operation.
     */
    @RequestMapping(value = "/operations/{name}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String name) {
    	// TODO return the entity upon delete
    	
        log.debug("REST request to delete Operation : {}", name);
        operationRepository.delete(name);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("operation", name.toString())).build();
    }

}
