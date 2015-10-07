package io.shick.shiken.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "JHI_USER_PROJECT_AUTHORITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectAuthority implements Serializable {

  @Id
  @NotNull
    @Column(name="project_id")
    private Long id;

    @Id
    @NotNull
    @Size(min = 0, max = 50)
    @Column(name="role_name", length = 50)
    private String roleName;

    @Id
    @NotNull
    @Column(name="user_id")
    private Long userId;

}
