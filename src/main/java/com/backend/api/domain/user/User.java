package com.backend.api.domain.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document("user")
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private String id;
    private String name;
    private String work;
}
