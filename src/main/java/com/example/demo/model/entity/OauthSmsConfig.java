package com.example.demo.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_sms_config")
@Data
public class OauthSmsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Column(name = "meta")
    private String meta;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "guid")
    private String guid;

    @Column(name = "hidden",columnDefinition = "TINYINT")
    private Boolean hidden;

}
