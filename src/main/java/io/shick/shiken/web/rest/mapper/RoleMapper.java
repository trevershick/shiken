package io.shick.shiken.web.rest.mapper;

import org.mapstruct.Mapper;

import io.shick.shiken.domain.Role;
import io.shick.shiken.web.rest.dto.RoleDTO;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {OperationMapper.class})
public interface RoleMapper {

    RoleDTO roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDTO roleDTO);
    
}
