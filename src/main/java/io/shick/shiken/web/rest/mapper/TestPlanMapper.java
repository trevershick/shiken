package io.shick.shiken.web.rest.mapper;

import io.shick.shiken.domain.*;
import io.shick.shiken.web.rest.dto.TestPlanDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestPlan and its DTO TestPlanDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TestPlanMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    TestPlanDTO testPlanToTestPlanDTO(TestPlan testPlan);

    @Mapping(source = "parentId", target = "parent")
    TestPlan testPlanDTOToTestPlan(TestPlanDTO testPlanDTO);

    default TestProject testProjectFromId(Long id) {
        if (id == null) {
            return null;
        }
        TestProject testProject = new TestProject();
        testProject.setId(id);
        return testProject;
    }
}
