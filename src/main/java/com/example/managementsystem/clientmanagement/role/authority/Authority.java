package com.example.managementsystem.clientmanagement.role.authority;

import com.example.managementsystem.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An authority (a security access level).
 */
@Data
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({JsonViews.Detailed.class})
    private Long id;

    @NotNull
    @Column(name = "name")
    @JsonView({JsonViews.Detailed.class})
    private String name;

    @Override
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    public String getAuthority() {
        return name.toUpperCase();
    }
}
























