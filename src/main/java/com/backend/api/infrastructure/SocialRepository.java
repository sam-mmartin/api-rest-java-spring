package com.backend.api.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.domain.Social;

public interface SocialRepository extends MongoRepository<Social, String> {

    @Query("{username:'?0'}")
    Optional<Social> findByUsername(String username);

    @Query("{userId:'?0'}")
    List<Social> findByUserId(String userId);

    public long count();
}
