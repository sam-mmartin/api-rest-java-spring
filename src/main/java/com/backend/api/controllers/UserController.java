package com.backend.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.api.domain.user.User;
import com.backend.api.infrastructure.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        var userList = repository.findAll("Developer");

        if (userList.size() > 0) {
            return ResponseEntity.ok(userList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> createUser() {
        var uuid = UUID.randomUUID();
        var user = new User(uuid.toString(), "Sam M. Martin", "Developer");
        repository.save(user);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
