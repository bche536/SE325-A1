package se325.assignment01.concert.service.domain;

import org.checkerframework.checker.fenum.qual.Fenum;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOOKINGS")
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "CONCERT_ID", nullable = false, length = 255)
    private long concertId;

    @Column(name = "DATE", nullable = false, length = 255)
    private LocalDateTime date;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Seat> seats = new ArrayList<>();

    public Booking() {

    }

    public Booking(User user, long concertId, LocalDateTime date, List<Seat> seats) {
        this.user = user;
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
