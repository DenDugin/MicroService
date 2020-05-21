package resume.microservice.com.ResumeService.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.microservice.com.ResumeService.Language.CurrentProfile;
import resume.microservice.com.ResumeService.elastic.ProfileSearchRepository;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.exception.UserServiceException;
import resume.microservice.com.ResumeService.repository.ProfileRepository;
import resume.microservice.com.ResumeService.service.FindProfileService;

import java.util.Optional;


@Service
public class FindProfileServiceImpl implements FindProfileService, UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindProfileServiceImpl.class);


    @Autowired
    private ProfileRepository profileRepository;


    @Autowired
    private ProfileSearchRepository profileSearchRepository;


    @Override
    public Profile findByUid(String uid) {
        return profileRepository.findByUid(uid);
    }

    @Override
    public Profile findOne(Long i) throws UserServiceException {

        Optional<Profile> result = profileRepository.findById(i);
        //  Optional --- шаблон вместо того, чтобы проверять на ноль
        // функция введена в Java 8

        if (result.isPresent())
            return result.get();
        else  throw new UserServiceException("No find user by id : "+ i);

    }




    // здесь реализация Пагинации : в аргументе указан объект, который описываете как сформировать страницу и
    // передаёте описание в запрос. Описание страницы инкапсулирует в себя класс PageRequest, у которого три конструктора:
    // PageRequest(int page, int size, Sort sort) —  указывается, какую страницу надо вернуть и какой размер страницы в строках
    // и дополнительно объект сортировки, задающий стабильный порядок строк
    @Override
    public Page<Profile> findAll(Pageable pageable) {
        return profileRepository.findAll(pageable);
    }




    @Override
    @Transactional
    public Iterable<Profile> findAllForIndexing() {
        Iterable<Profile> all = profileRepository.findAll();
        for(Profile p : all) {
            p.getSkills().size();
            p.getCertificates().size();
            p.getLanguages().size();
            p.getPractics().size();
            p.getCourses().size();
        }
        return all;
    }



    @Override
    public Page<Profile> findBySearchQuery(String query, Pageable pageable) {
        return profileSearchRepository.findByObjectiveLikeOrSummaryLikeOrPracticsCompanyLikeOrPracticsPositionLike(
                query, query, query, query, pageable);
    }

    @Override
    public Page<Profile> findByName(String query, Pageable pageable) {
        return profileSearchRepository.findByFirstNameLikeOrLastNameLike( query, query, pageable);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = findProfile(username);
        if (profile != null) {
            return new CurrentProfile(profile);
        } else {
            LOGGER.error("Profile not found by " + username);
            throw new UsernameNotFoundException("Profile not found by " + username);
        }
    }


    private Profile findProfile(String anyUnigueId) {
        Profile profile = profileRepository.findByUid(anyUnigueId);
        if (profile == null) {
            profile = profileRepository.findByEmail(anyUnigueId);
            if (profile == null) {
                profile = profileRepository.findByPhone(anyUnigueId);
            }
        }
        return profile;
    }



}