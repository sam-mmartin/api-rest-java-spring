package com.backend.api.aplication.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.api.aplication.dto.user.UserDataRequest;
import com.backend.api.aplication.dto.user.UserDataUpdate;
import com.backend.api.aplication.dto.social.SocialDataRequest;
import com.backend.api.aplication.dto.user.UserDataFullUpdate;
import com.backend.api.aplication.dto.user.UserFullDataRequest;
import com.backend.api.aplication.dto.user.UserSocialUpdate;
import com.backend.api.domain.User;
import com.backend.api.infrastructure.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private SocialService socialService;

    // #region Create
    public User saveUser(UserDataRequest dataRequest) {
        var uuid = UUID.randomUUID();
        var user = new User(uuid.toString(),
                dataRequest.name(),
                dataRequest.work());

        var newUser = repository.save(user);

        return newUser;
    }

    public User saveFullUser(UserFullDataRequest dataRequest) {
        var user = saveUser(new UserDataRequest(dataRequest.name(), dataRequest.work()));
        socialService.saveAllFullSocialMedia(dataRequest.socials(), user);

        var socials = socialService.getSocialMediasByUser(user.getId());

        user.setSocialMedias(socials);

        var userFull = repository.save(user);
        return userFull;
    }
    // #endregion

    // #region Update
    public User update(UserDataUpdate userData) {
        var user = repository.findById(userData.id());

        user.ifPresent((userUpdate) -> {
            userUpdate.update(userData.name(), userData.work());
            repository.save(userUpdate);
        });

        return user.orElse(null);
    }

    public User updateSocialMediasByUser(UserSocialUpdate userData) {
        var user = repository.findById(userData.id());

        user.ifPresent((userUpdate) -> {
            System.out.println(userUpdate.getName());

            boolean socialExists = userData.socials() != null && userData.socials().size() > 0;

            if (socialExists) {
                userData.socials().forEach((item) -> {
                    var social = socialService.getSocialById(item.id());

                    social.ifPresentOrElse((media) -> {
                        socialService.updateSocialMedia(item.id(),
                                item.username(),
                                item.link());
                    }, () -> {
                        var newSocial = socialService.saveSocialMedia(userUpdate.getId(),
                                new SocialDataRequest(
                                        item.name(),
                                        item.username(),
                                        item.link()));

                        userUpdate.addSocialMedia(newSocial);
                    });
                });

                repository.save(userUpdate);
            }
        });

        return user.orElse(null);
    }

    public User updateFullUser(UserDataFullUpdate dataUpdate) {
        var user = repository.findById(dataUpdate.id());

        user.ifPresent((userUpdate) -> {
            userUpdate.update(dataUpdate.name(), dataUpdate.work());
            boolean socialExists = dataUpdate.socials() != null && dataUpdate.socials().size() > 0;

            if (socialExists) {
                dataUpdate.socials().forEach((item) -> {
                    var social = socialService.updateSocialMedia(item.id(),
                            item.username(),
                            item.link());

                    if (!Objects.isNull(social)) {
                        userUpdate.addSocialMedia(social);
                    }
                });
            }

            repository.save(userUpdate);
        });

        return user.orElse(null);
    }
    // #endregion

    // #region Read
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(String id) {
        var user = repository.findById(id);

        return user.orElse(null);
    }

    public User getUserByName(String name) {
        return repository.findUserByName(name);
    }
    // #endregion

    // #region
    public void deleteUserById(String id) {
        repository.deleteById(id);
    }
    // #endregion
}
