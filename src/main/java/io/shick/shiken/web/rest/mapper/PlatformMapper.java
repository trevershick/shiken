package io.shick.shiken.web.rest.mapper;

import io.shick.shiken.domain.*;
import io.shick.shiken.web.rest.dto.PlatformDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Platform and its DTO PlatformDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlatformMapper {

    PlatformDTO platformToPlatformDTO(Platform platform);

    Platform platformDTOToPlatform(PlatformDTO platformDTO);
}
