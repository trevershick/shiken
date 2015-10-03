package io.shick.shiken.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the TestProject entity.
 */
public class TestProjectDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    private String name;

    private String description;

    @NotNull
    @Size(min = 2, max = 4)
    private String prefix;

    private Boolean active;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestProjectDTO testProjectDTO = (TestProjectDTO) o;

        if ( ! Objects.equals(id, testProjectDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestProjectDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", prefix='" + prefix + "'" +
                ", active='" + active + "'" +
                '}';
    }
}
