package com.backend.api.aplication.dto.user;

import java.util.List;

import com.backend.api.aplication.dto.social.SocialDataRequest;

public record UserFullDataRequest(String name, String work, List<SocialDataRequest> socials) {

}
