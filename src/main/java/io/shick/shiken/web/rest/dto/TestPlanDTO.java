package io.shick.shiken.web.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * A DTO for the TestPlan entity.
 */
@AutoProperty
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
