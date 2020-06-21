package resume.microservice.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import resume.microservice.entity.SkillCategory;

import java.util.List;



public interface SkillCategoryRepository  extends JpaRepository<SkillCategory, Long> {

    List<SkillCategory> findAllByOrderById();

    List<SkillCategory> findAll(Sort sort);

}