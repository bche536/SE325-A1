package se325.assignment01.concert.service.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOOKINGS")
public class Booking {

    @Id
    @Column
    private long concertId;

    @Column
    private LocalDateTime date;

    @OneToMany
    @CollectionTable(name = "BOOKING_SEATS", joinColumns = @JoinColumn(name = "BOOKING_ID"))
    @Column(name = "SEATS")
    private List<Seat> seat = new ArrayList<>();

    public Booking() {

    }

    public Booking(long concertId, LocalDateTime date, List<Seat> seats) {
        this.concertId = concertId;
        this.date = date;
        this.seat = seats;
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

    public List<Seat> getSeat() {
        return seat;
    }

    public void setSeat(List<Seat> seats) {
        this.seat = seats;
    }

}
