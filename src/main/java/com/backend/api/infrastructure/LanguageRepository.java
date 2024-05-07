package com.backend.api.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.domain.Language;

public interface LanguageRepository extends MongoRepository<Language, String> {

    @Query("{name:'?0'}")
    Language findLanguageByName(String name);

    public long count();
}
