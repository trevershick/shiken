package io.shick.shiken.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * A Platform.
 */
@Entity
@Table(name = "PLATFORM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="platform")
public class Platform implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1623385530772987652L;

	@Id
    @Size(min = 2, max = 8)    
    @Column(name = "name", length = 8, nullable = false)
	@Property
    private String name;


    
    @Column(name = "description")
	@Property
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
