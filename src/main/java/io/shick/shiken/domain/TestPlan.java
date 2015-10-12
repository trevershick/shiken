package io.shick.shiken.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * A TestPlan.
 */
@Entity
@Table(name = "TESTPLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="testplan")
public class TestPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2610740732839255101L;

	@Property
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Property
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

	@Property
    @Column(name = "description")
    private String description;

	@Property
    @Column(name = "active")
    private Boolean active;

	@Property
    @Column(name = "apikey")
    private String apikey;

    @ManyToOne
    private TestProject parent;

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

    public TestProject getParent() {
        return parent;
    }

    public void setParent(TestProject testProject) {
        this.parent = testProject;
    }

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}
	@Override
	public int hashCode() {
		return Pojomatic.hashCode(this);
	}
	@Override
	public boolean equals(Object other) {
		return Pojomatic.equals(this, other);
	}
}
