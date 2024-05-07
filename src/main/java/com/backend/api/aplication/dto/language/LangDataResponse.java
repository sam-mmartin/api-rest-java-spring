package com.backend.api.aplication.dto.language;

import java.time.LocalDate;

import com.backend.api.domain.Language;

public record LangDataResponse(String id, String name, String image, LocalDate startDateUse) {

    public LangDataResponse() {
        this("", "", "", LocalDate.now());
    }

    public LangDataResponse(Language language) {
        this(language.getId(),
                language.getName(),
                language.getImage(),
                language.getStartDateUse());
    }
}
