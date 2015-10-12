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

import io.shick.shiken.domain.Role;
import io.shick.shiken.repository.RoleRepository;
import io.shick.shiken.web.rest.dto.RoleDTO;
import io.shick.shiken.web.rest.mapper.RoleMapper;
import io.shick.shiken.web.rest.util.HeaderUtil;
import io.shick.shiken.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private RoleMapper roleMapper;

    /**
     * POST  /roles -> Create a new role.
     */
    @RequestMapping(value = "/roles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDTO);
        // if (roleDTO.getName() != null) {
        //     return ResponseEntity.badRequest().header("Failure", "A new role cannot already have an ID").body(null);
        // }
        Role role = roleMapper.roleDtoToRole(roleDTO);
        Role result = roleRepository.save(role);
        return ResponseEntity.created(new URI("/api/roles/" + result.getName()))
                .headers(HeaderUtil.createEntityCreationAlert("role", result.getName().toString()))
                .body(roleMapper.roleToRoleDto(result));
    }

    /**
     * PUT  /roles -> Updates an existing role.
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoleDTO> update(@Valid @RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to update Role : {}", roleDTO);
        if (roleDTO.getName() == null) {
            return create(roleDTO);
        }
        Role role = roleMapper.roleDtoToRole(roleDTO);
        Role result = roleRepository.save(role);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("role", roleDTO.getName().toString()))
                .body(roleMapper.roleToRoleDto(result));
    }

    /**
     * GET  /roles -> get all the roles.
     */
    @RequestMapping(value = "/roles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RoleDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Role> page = roleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(roleMapper::roleToRoleDto)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /roles/:id -> get the "id" role.
     */
    @RequestMapping(value = "/roles/{name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoleDTO> get(@PathVariable String name) {
        log.debug("REST request to get Role : {}", name);
        return Optional.ofNullable(roleRepository.findOne(name))
            .map(roleMapper::roleToRoleDto)
            .map(roleDTO -> new ResponseEntity<>(
                roleDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /roles/:name -> delete the "id" role.
     */
    @RequestMapping(value = "/roles/{name}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String name) {
    	// TODO return the entity upon delete
	
        log.debug("REST request to delete Role : {}", name);
        roleRepository.delete(name);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("role", name.toString())).build();
    }

}
