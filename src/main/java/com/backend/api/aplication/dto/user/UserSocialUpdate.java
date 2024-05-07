package com.backend.api.aplication.dto.user;

import java.util.List;

import com.backend.api.aplication.dto.social.SocialDataUpdate;

public record UserSocialUpdate(String id, List<SocialDataUpdate> socials) {

}
