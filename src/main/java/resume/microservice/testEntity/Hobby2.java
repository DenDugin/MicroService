package resume.microservice.testEntity;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="hobby")
public class Hobby2 implements Serializable{


    @Id
    @SequenceGenerator(name="HOBBY_ID_GENERATOR", sequenceName="HOBBY_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="HOBBY_ID_GENERATOR")
    @Column(unique=true, nullable=false)
    private Long id;

    @Column(nullable=false, length=30)
    private String name;


    public Hobby2() {
    }

    public Hobby2(String name) {
        super();
        this.name = name;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Transient
    public String getCssClassName(){
        return name.replace(" ", "-").toLowerCase();
    }



    @Override
    public String toString() {
        return String.format("Hobby [name=%s]", name);
    }
}
