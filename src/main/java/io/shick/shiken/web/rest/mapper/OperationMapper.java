package io.shick.shiken.web.rest.mapper;

import org.mapstruct.Mapper;

import io.shick.shiken.domain.Operation;
import io.shick.shiken.web.rest.dto.OperationDTO;

/**
 * Mapper for the entity Operation and its DTO OperationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OperationMapper {

    OperationDTO operationToOperationDto(Operation operation);

    Operation operationDtoToOperation(OperationDTO operationDTO);
}
