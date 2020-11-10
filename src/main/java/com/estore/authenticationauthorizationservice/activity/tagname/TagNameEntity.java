package com.estore.authenticationauthorizationservice.activity.tagname;

import com.estore.authenticationauthorizationservice.activity.ActivityEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tag_name")
public class TagNameEntity {

    @Id
    @Column(name = "key")
    private String key;

    @Column(name = "locale")
    private String locale;

    @Column(name = "message")
    private String message;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activity;
}
