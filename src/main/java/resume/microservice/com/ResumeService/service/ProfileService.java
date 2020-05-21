package resume.microservice.com.ResumeService.service;

import resume.microservice.com.ResumeService.entity.Profile;

import java.util.Optional;

public interface ProfileService {

    Profile findById(Long id);

}
