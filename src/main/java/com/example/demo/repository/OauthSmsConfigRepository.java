package com.example.demo.repository;

import com.example.demo.model.entity.OauthSmsConfig;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OauthSmsConfigRepository extends CrudRepository<OauthSmsConfig,Long> {
    Optional<OauthSmsConfig> findByName(String name);

}
