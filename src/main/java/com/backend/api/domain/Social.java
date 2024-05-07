package com.backend.api.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document("social")
@Getter
@Setter
public class Social {

    @Id
    private String id;
    private String name;
    private String username;
    private String link;
    private String userId;

    public Social() {
    }

    public Social(String id, String name, String username, String link) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.link = link;
    }

    public Social(String id, String name, String username, String link, String userId) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.link = link;
        this.userId = userId;
    }

    public void update(String username, String link) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }

        if (link != null && !link.isEmpty()) {
            this.link = link;
        }
    }

    public static Social createSocial(String name, String username, String link) {
        var uuid = UUID.randomUUID();
        return new Social(uuid.toString(), name, username, link);
    }
}
