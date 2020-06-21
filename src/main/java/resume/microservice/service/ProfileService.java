package resume.microservice.service;

import resume.microservice.entity.Profile;

public interface ProfileService {

    Profile findById(Long id);

}
