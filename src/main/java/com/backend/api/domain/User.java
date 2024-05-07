package com.backend.api.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document("user")
@Getter
@Setter
public class User {

    @Id
    private String id;
    private String name;
    private String work;

    @DBRef
    private List<Social> socialMedias;

    public User() {
    }

    public User(String id, String name, String work) {
        this.id = id;
        this.name = name;
        this.work = work;
    }

    public User(String id, String name, String work, List<Social> socials) {
        this.id = id;
        this.name = name;
        this.work = work;
        this.socialMedias = socials;
    }

    public void update(String name, String work) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        if (work != null && !work.isEmpty()) {
            this.work = work;
        }
    }

    public void addSocialMedia(Social social) {
        if (Objects.isNull(socialMedias)) {
            this.socialMedias = new ArrayList<>();
        }

        if (!this.socialMedias.contains(social)) {
            this.socialMedias.add(social);
        }
    }
}
