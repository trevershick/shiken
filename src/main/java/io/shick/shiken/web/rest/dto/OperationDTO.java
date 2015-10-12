package io.shick.shiken.web.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;


/**
 * A DTO for the Keyword entity.
 */
@AutoProperty
public class OperationDTO implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -2686021689023935959L;

	@NotNull
    @Size(min = 0, max = 15)
	private String name;

	@NotNull
    @Size(min = 4, max = 25)
	private String title;

	private String description;

	@NotNull
	private String groupName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
