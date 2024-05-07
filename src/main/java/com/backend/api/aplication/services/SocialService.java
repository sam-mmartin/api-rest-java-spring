package com.backend.api.aplication.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.api.aplication.dto.social.SocialDataRequest;
import com.backend.api.domain.Social;
import com.backend.api.domain.User;
import com.backend.api.infrastructure.SocialRepository;

@Service
public class SocialService {

    @Autowired
    private SocialRepository repository;

    // #region Create
    public Social saveSocialMedia(String userId, SocialDataRequest socialData) {
        var newSocial = Social.createSocial(socialData.name(),
                socialData.username(),
                socialData.link());

        if (userId != null && !userId.isEmpty()) {
            newSocial.setUserId(userId);
        }

        return repository.save(newSocial);
    }

    public void saveAllFullSocialMedia(List<SocialDataRequest> dataRequests, User user) {
        dataRequests.forEach((social) -> {
            saveSocialMedia(user.getId(), social);
        });
    }
    // #endregion

    // #region Update
    public Social updateSocialMedia(String id, String username, String link) {
        var social = repository.findById(id);

        social.ifPresent((item) -> {
            item.update(username, link);
            repository.save(item);
        });

        return social.orElse(null);
    }
    // #endregion

    // #region Read
    public List<Social> getAllSocialMedias() {
        return repository.findAll();
    }

    public Optional<Social> getSocialById(String id) {
        return repository.findById(id);
    }

    public List<Social> getSocialMediasByUser(String userId) {
        return repository.findByUserId(userId);
    }
    // #endregion

}
