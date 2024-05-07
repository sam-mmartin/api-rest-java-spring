package com.backend.api.aplication.dto.user;

import java.util.List;

import com.backend.api.aplication.dto.social.SocialUserDataRequest;

public record UserDataFullUpdate(String id, String name, String work, List<SocialUserDataRequest> socials) {

}
