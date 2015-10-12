package io.shick.shiken.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A TestProject.
 */
@Entity
@Table(name = "TESTPROJECT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "testproject")
public class TestProject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7835956913847429913L;

	@Property
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Property
	@NotNull
	@Size(min = 4, max = 20)
	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@Property
	@Column(name = "description")
	private String description;

	@Property
	@NotNull
	@Size(min = 2, max = 4)
	@Column(name = "prefix", length = 4, nullable = false)
	private String prefix;

	@Property
	@Column(name = "active")
	private Boolean active;

	@OneToMany(mappedBy="project")
	private List<ProjectRole> users;
	
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
