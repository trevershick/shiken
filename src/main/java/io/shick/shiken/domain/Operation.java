package io.shick.shiken.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pojomatic.annotations.Property;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "JHI_OPERATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8783689208002274800L;


	@Id
	@Property
	@NotNull
    @Size(min = 0, max = 15)
    @Column(length = 15)
	String name;
	
	@NotNull
    @Size(min = 4, max = 25)
    @Column(length = 25,nullable=true)
	private String title;

    @Column
	private String description;
	
	@NotNull
	@Column
	String groupName;

	
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
	

}
