package resume.microservice.com.ResumeService.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.repository.ProfileRepository;
import resume.microservice.com.ResumeService.service.ProfileService;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile findById(Long id) {

        Optional<Profile> result = profileRepository.findById(id);
        //  Optional --- шаблон вместо того, чтобы проверять на ноль
        // функция введена в Java 8

        if (result.isPresent())
            return result.get();
        else  throw new RuntimeException("No find user by id : "+ id);

    }

}
