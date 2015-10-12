package io.shick.shiken.domain;

import java.io.Serializable;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;


public class ProjectRoleId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6013837506956373262L;

	@Property
	private long userId;

	@Property
	private long projectId;
	
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
