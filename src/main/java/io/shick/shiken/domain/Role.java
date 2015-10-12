package io.shick.shiken.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "JHI_AUTHORITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2554303810575441921L;

	@Property
	@NotNull
    @Size(min = 0, max = 50)
    @Id
    @Column(length = 12)
    private String name;

	@Property
	@NotNull
    @Size(min = 4, max = 20)
    @Column(length = 20)
    private String title;

	@Property
    @Size(min = 0, max = 150)
    @Column(length = 150)
    private String description;

	
	
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



	@Property(policy=PojomaticPolicy.TO_STRING)
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "JHI_ROLE_OPERATION",
            joinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")},
            inverseJoinColumns = {@JoinColumn(name = "op_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Operation> operations = new HashSet<>();

	public Set<Operation> getOperations() {
		return operations;
	}

	public void setOperations(Set<Operation> authorities) {
		this.operations = authorities;
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
