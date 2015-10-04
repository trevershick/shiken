package io.shick.shiken.web.rest.mapper;

import io.shick.shiken.domain.*;
import io.shick.shiken.web.rest.dto.KeywordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Keyword and its DTO KeywordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KeywordMapper {

    KeywordDTO keywordToKeywordDTO(Keyword keyword);

    Keyword keywordDTOToKeyword(KeywordDTO keywordDTO);
}
