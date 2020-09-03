package resume.microservice.testRep;


import org.springframework.data.jpa.repository.JpaRepository;
import resume.microservice.repository.AbstractProfileEntityRepository;
import resume.microservice.testEntity.Hobby2;

public interface HobbyRepository2 extends JpaRepository<Hobby2, Long> {

}
