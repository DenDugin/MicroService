package resume.microservice.com.ResumeService.elastic;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import resume.microservice.com.ResumeService.entity.Profile;



public interface ProfileSearchRepository extends ElasticsearchRepository<Profile, Long> {

    /**
     *
     * http://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.query-methods.criterions
     */

    // поиск слова в указанных категориях
    Page<Profile> findByObjectiveLikeOrSummaryLikeOrPracticsCompanyLikeOrPracticsPositionLike(
            String objective, String summary, String practicCompany, String practicPosition, Pageable pageable);


    Page<Profile> findByFirstNameLikeOrLastNameLike(
            String firstName, String lastName, Pageable pageable);



}