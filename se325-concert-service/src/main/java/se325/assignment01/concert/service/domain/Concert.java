package se325.assignment01.concert.service.domain;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "CONCERTS")
public class Concert {

    // TODO Implement this class.

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, length = 255)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;

    @Column(name = "IMAGE_NAME", nullable = false, length = 255)
    private String imageName;

    @Column(name = "BLURB", nullable = false, length = 1024)
    private String blurb;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCERT_DATES")
    @Column(name = "DATE")
    @Fetch(FetchMode.SUBSELECT)
    private Set<LocalDateTime> dates = new HashSet<>();

    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    @CollectionTable(name = "CONCERT_PERFORMER")
    @Column(name = "PERFORMER_ID")
    private Set<Performer> performers = new HashSet<>();

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;

    }

    public Concert() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return this.blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Set<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates (Set<LocalDateTime> dates) {
        this.dates = dates;
    }


    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformer(Set<Performer> dates) {
        this.performers = performers;
    }


}
