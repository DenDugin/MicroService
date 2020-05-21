package resume.microservice.com.ResumeService.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import resume.microservice.com.ResumeService.entity.Profile;

import java.sql.Timestamp;
import java.util.List;


// public interface ProfileRepository  extends JpaRepository<Profile, Long> {
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long> {

    Profile findByUid(String uid);

    Profile findByEmail(String email);

    Profile findByPhone(String phone);

    int countByUid(String uid);

    Page<Profile> findAllByCompletedTrue(Pageable pageable);

    List<Profile> findByCompletedFalseAndCreatedBefore(Timestamp oldDate);



}
