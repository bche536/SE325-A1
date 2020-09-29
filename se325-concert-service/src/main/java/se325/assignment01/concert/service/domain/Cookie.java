package se325.assignment01.concert.service.domain;

import javax.persistence.*;

@Entity
@Table(name = "COOKIES")
public class Cookie {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, length = 255)
    private long id;

    @Column(name = "USERNAME", nullable = false, length = 255)
    private String username;

    public Cookie () {

    }

    public Cookie (String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
