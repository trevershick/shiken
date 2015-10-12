package io.shick.shiken.web.rest.dto;

import java.io.Serializable;
import java.util.Set;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;


/**
 * A DTO for the Keyword entity.
 */
@AutoProperty
public class RoleDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8179001245209644244L;

	private String name;
	
	private String title;

    private String description;

    private Set<OperationDTO> operations;
    
    public Set<OperationDTO> getOperations() {
		return operations;
	}

	public void setOperations(Set<OperationDTO> operations) {
		this.operations = operations;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
