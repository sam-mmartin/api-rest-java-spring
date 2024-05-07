package com.backend.api.infrastructure;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{name:'?0'}")
    User findUserByName(String name);

    // A definição FIELDS informa que só queremos o campo NAME
    // na resposta da consulta
    @Query(value = "{work:'?0'}", fields = "{'name': 1, 'work': 1}")
    List<User> findAllByWork(String work);

    public long count();
}
