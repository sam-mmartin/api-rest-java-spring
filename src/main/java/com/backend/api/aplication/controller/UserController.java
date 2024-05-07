package com.backend.api.aplication.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.api.aplication.dto.user.UserDataFullUpdate;
import com.backend.api.aplication.dto.user.UserDataRequest;
import com.backend.api.aplication.dto.user.UserDataUpdate;
import com.backend.api.aplication.dto.user.UserFullDataRequest;
import com.backend.api.aplication.dto.user.UserSocialUpdate;
import com.backend.api.aplication.services.UserService;
import com.backend.api.domain.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    // #region Read
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        var userList = service.getAllUsers();

        if (userList.size() > 0) {
            return ResponseEntity.ok(userList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id) {
        var user = service.getUserById(id);

        if (Objects.isNull(user)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<User> findByName(@PathVariable String name) {
        var user = service.getUserByName(name);

        if (Objects.isNull(user)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
    // #endregion

    // #region Create
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDataRequest dataRequest) {
        var user = service.saveUser(dataRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/full")
    public ResponseEntity<Object> createFullUser(@RequestBody UserFullDataRequest dataRequest) {
        var user = service.saveFullUser(dataRequest);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    // #endregion

    // #region Update
    @PutMapping
    public ResponseEntity<User> update(@RequestBody UserDataUpdate userData) {
        var user = service.update(userData);

        if (Objects.isNull(user)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PutMapping("/full")
    public ResponseEntity<Object> fullUpdate(@RequestBody UserDataFullUpdate userData) {
        var user = service.updateFullUser(userData);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/social-medias")
    public ResponseEntity<User> update(@RequestBody UserSocialUpdate userData) {
        var user = service.updateSocialMediasByUser(userData);

        if (Objects.isNull(user)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
    // #endregion

    // #region Delete
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        service.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
    // #endregion
}
