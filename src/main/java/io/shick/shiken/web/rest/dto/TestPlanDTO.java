package io.shick.shiken.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TestPlan entity.
 */
public class TestPlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private Boolean active;

    private String apikey;

    private Long parentId;

    private String parentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long testProjectId) {
        this.parentId = testProjectId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String testProjectName) {
        this.parentName = testProjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestPlanDTO testPlanDTO = (TestPlanDTO) o;

        if ( ! Objects.equals(id, testPlanDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestPlanDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", active='" + active + "'" +
                ", apikey='" + apikey + "'" +
                '}';
    }
}
