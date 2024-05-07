package com.backend.api.domain;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.backend.api.aplication.dto.language.LangDataUpdateRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document("language")
@AllArgsConstructor
@Getter
@Setter
public class Language {

    @Id
    private String id;
    private String name;
    private String image;
    private LocalDate startDateUse;

    public void update(LangDataUpdateRequest language) {
        if (!Objects.isNull(language.name())) {
            this.name = language.name();
        }

        if (!Objects.isNull(language.image())) {
            this.image = language.image();
        }
    }
}
