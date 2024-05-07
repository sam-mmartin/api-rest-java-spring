package com.backend.api.aplication.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.api.aplication.dto.social.SocialDataRequest;
import com.backend.api.aplication.services.SocialService;
import com.backend.api.domain.Social;

@RestController
@RequestMapping("/socials")
public class SocialController {

    @Autowired
    private SocialService service;

    @PostMapping
    @Transactional
    public ResponseEntity<Object> create(@RequestBody SocialDataRequest socialData) {
        var social = service.saveSocialMedia(null, socialData);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(social.getId().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Social>> getAll() {
        var socials = service.getAllSocialMedias();

        if (socials.size() > 0) {
            return ResponseEntity.ok(socials);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Social>> getAllSocialByUser(@PathVariable String userId) {
        var socials = service.getSocialMediasByUser(userId);

        System.out.println(socials.iterator());

        if (socials.size() > 0) {
            return ResponseEntity.ok(socials);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
