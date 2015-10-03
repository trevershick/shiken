package io.shick.shiken.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A TestProject.
 */
@Entity
@Table(name = "TESTPROJECT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="testproject")
public class TestProject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)    

    
    @Column(name = "name", length = 20, nullable = false)
    private String name;


    
    @Column(name = "description")
    private String description;

    @NotNull
    @Size(min = 2, max = 4)    

    
    @Column(name = "prefix", length = 4, nullable = false)
    private String prefix;


    
    @Column(name = "active")
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

        TestProject testProject = (TestProject) o;

        if ( ! Objects.equals(id, testProject.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestProject{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", prefix='" + prefix + "'" +
                ", active='" + active + "'" +
                '}';
    }
}
