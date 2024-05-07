package com.backend.api.aplication.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.api.aplication.dto.language.LangDataRequest;
import com.backend.api.aplication.dto.language.LangDataResponse;
import com.backend.api.aplication.dto.language.LangDataUpdateRequest;
import com.backend.api.aplication.services.LanguageService;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    @Autowired
    private LanguageService service;

    // #region Read
    @GetMapping
    public ResponseEntity<List<LangDataResponse>> findAll() {
        var languages = service.getAllLanguages();

        if (languages.size() > 0) {
            return ResponseEntity.ok(languages);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<LangDataResponse> findByName(@PathVariable String name) {
        var response = service.getLangByName(name);

        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<LangDataResponse> findById(@PathVariable String id) {
        var response = service.getLangById(id);

        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
    // #endregion

    // #region Create
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody LangDataRequest dataRequestPost) {
        var lang = service.saveLanguage(dataRequestPost);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lang.getId().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<LangDataResponse> update(@RequestBody @Validated LangDataUpdateRequest dataRequestPut) {
        var language = service.updateLanguage(dataRequestPut);

        if (language.isPresent()) {
            return ResponseEntity.ok(new LangDataResponse(language.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
