package io.shick.shiken.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Keyword entity.
 */
public class KeywordDTO implements Serializable {

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeywordDTO keywordDTO = (KeywordDTO) o;

        if ( ! Objects.equals(name, keywordDTO.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "KeywordDTO{" +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
