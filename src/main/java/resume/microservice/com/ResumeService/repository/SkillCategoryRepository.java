package resume.microservice.com.ResumeService.repository;


import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import resume.microservice.com.ResumeService.entity.SkillCategory;
import java.util.List;



public interface SkillCategoryRepository  extends JpaRepository<SkillCategory, Long> {

    List<SkillCategory> findAllByOrderById();

    List<SkillCategory> findAll(Sort sort);

}