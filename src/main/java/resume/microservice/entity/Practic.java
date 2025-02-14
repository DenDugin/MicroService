package resume.microservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Entity
@Table(name = "practic")
public class Practic extends AbstractFinishDateEntity<Long> implements Serializable, ProfileEntity {


    @Id
    @SequenceGenerator(name = "PRACTIC_ID_GENERATOR", sequenceName = "PRACTIC_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRACTIC_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(length = 255)
    private String demo;

    @Column(length = 255)
    private String src;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(nullable = false, length = 2147483647)
    private String responsibilities;

    @Column(name = "begin_date", nullable = false)
    private Date beginDate;

//    @Column(name = "finish_date", nullable = false)
//    private Date finishDate;


    @Transient
    private Integer beginDateMonth;

    @Transient
    private Integer beginDateYear;

    // bi-directional many-to-one association to Profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profile", nullable = false)
    @JsonIgnore
    private Profile profile;

    public Practic() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDemo() {
        return this.demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getResponsibilities() {
        return this.responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

//    @Override
//    public Date getFinishDate() {
//        return finishDate;
//    }
//
//    @Override
//    public void setFinishDate(Date finishDate) {
//        this.finishDate = finishDate;
//    }

    @Transient
    public Integer getBeginDateMonth() {
        if (beginDate != null) {
            return new DateTime(beginDate).getMonthOfYear();
        } else {
            return null;
        }
    }

    @Transient
    public Integer getBeginDateYear() {
        if (beginDate != null) {
            return new DateTime(beginDate).getYear();
        } else {
            return null;
        }
    }

    public void setBeginDateMonth(Integer beginDateMonth) {
        this.beginDateMonth = beginDateMonth;
        setupBeginDate();
    }

    public void setBeginDateYear(Integer beginDateYear) {
        this.beginDateYear = beginDateYear;
        setupBeginDate();
    }

    private void setupBeginDate() {
        if (beginDateYear != null && beginDateMonth != null) {
            setBeginDate(new Date(new DateTime(beginDateYear, beginDateMonth, 1, 0, 0).getMillis()));
        } else {
            setBeginDate(null);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((beginDate == null) ? 0 : beginDate.hashCode());
        result = prime * result + ((company == null) ? 0 : company.hashCode());
        result = prime * result + ((demo == null) ? 0 : demo.hashCode());
        result = prime * result + ((getFinishDate() == null) ? 0 : getFinishDate().hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((responsibilities == null) ? 0 : responsibilities.hashCode());
        result = prime * result + ((src == null) ? 0 : src.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof Practic))
            return false;
        Practic other = (Practic) obj;
        if (beginDate == null) {
            if (other.beginDate != null)
                return false;
        } else if (!beginDate.equals(other.beginDate))
            return false;
        if (company == null) {
            if (other.company != null)
                return false;
        } else if (!company.equals(other.company))
            return false;
        if (demo == null) {
            if (other.demo != null)
                return false;
        } else if (!demo.equals(other.demo))
            return false;
        if (getFinishDate() == null) {
            if (other.getFinishDate() != null)
                return false;
        } else if (!getFinishDate().equals(other.getFinishDate()))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (responsibilities == null) {
            if (other.responsibilities != null)
                return false;
        } else if (!responsibilities.equals(other.responsibilities))
            return false;
        if (src == null) {
            if (other.src != null)
                return false;
        } else if (!src.equals(other.src))
            return false;
        return true;
    }
}
