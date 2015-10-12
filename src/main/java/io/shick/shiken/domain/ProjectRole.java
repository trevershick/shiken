package io.shick.shiken.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="JHI_PROJECT_ROLE")
@IdClass(ProjectRoleId.class)
public class ProjectRole {
	@Id
    private Long userId;
	
	@Id
	private Long projectId;
	
	@ManyToOne
	@JoinColumn(name="role_name", referencedColumnName="name")
    private Role role;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="project_id", referencedColumnName="id")
	private TestProject project;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TestProject getProject() {
		return project;
	}

	public void setProject(TestProject project) {
		this.project = project;
	}
	
	
	
	
}
