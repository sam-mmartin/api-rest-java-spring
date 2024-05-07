package com.backend.api.aplication.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.aplication.dto.language.LangDataRequest;
import com.backend.api.aplication.dto.language.LangDataResponse;
import com.backend.api.aplication.dto.language.LangDataUpdateRequest;
import com.backend.api.domain.Language;
import com.backend.api.infrastructure.LanguageRepository;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository repository;

    // #region Read
    public List<LangDataResponse> getAllLanguages() {
        var languages = repository.findAll().stream().map(LangDataResponse::new).toList();

        return languages;
    }

    public LangDataResponse getLangById(String id) {
        var language = repository.findById(id);
        var lang = language.orElse(null);

        if (Objects.isNull(lang)) {
            return null;
        } else {
            return new LangDataResponse(lang.getId(),
                    lang.getName(),
                    lang.getImage(),
                    lang.getStartDateUse());
        }
    }

    public LangDataResponse getLangByName(String name) {
        var language = repository.findLanguageByName(name);
        var langDataRes = new LangDataResponse(language);

        return langDataRes;
    }
    // #endregion

    public Language saveLanguage(LangDataRequest dataRequestPost) {
        var uuid = UUID.randomUUID();
        var dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        var startDateUse = LocalDate.parse(dataRequestPost.startDateUse(), dateFormatter);
        var language = new Language(
                uuid.toString(),
                dataRequestPost.name(),
                dataRequestPost.image(),
                startDateUse);
        var lang = repository.save(language);

        return lang;
    }

    @Transactional
    public Optional<Language> updateLanguage(LangDataUpdateRequest dataRequestPut) {
        Optional<Language> language = repository.findById(dataRequestPut.id());

        language.ifPresent((lang) -> {
            lang.update(dataRequestPut);
            repository.save(lang);
        });

        return language;
    }
}
