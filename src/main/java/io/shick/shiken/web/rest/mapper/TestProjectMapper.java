package io.shick.shiken.web.rest.mapper;

import io.shick.shiken.domain.*;
import io.shick.shiken.web.rest.dto.TestProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestProject and its DTO TestProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TestProjectMapper {

    TestProjectDTO testProjectToTestProjectDTO(TestProject testProject);

    TestProject testProjectDTOToTestProject(TestProjectDTO testProjectDTO);
}
