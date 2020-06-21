package resume.microservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import resume.microservice.entity.Profile;


// сервис для поиска и отображения профиля
public interface FindProfileService {

    Profile findByUid(String uid);

    Profile findOne(Long i);

    Page<Profile> findAll(Pageable pageable);

    Iterable<Profile> findAllForIndexing(); // ищет профили для индексации

    Page<Profile> findBySearchQuery(String query, Pageable pageable);

    Page<Profile> findByName (String query, Pageable pageable);

}