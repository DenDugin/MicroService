package resume.microservice.com.ResumeService.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.repository.ProfileRepository;
import resume.microservice.com.ResumeService.service.ProfileService;


@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    public Profile findById(Long id) {

        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("No find user by id : "+ id));

        return profile;

    }





}
