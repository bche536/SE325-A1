package se325.assignment01.concert.service.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dom4j.QName;
import se325.assignment01.concert.common.jackson.LocalDateTimeDeserializer;
import se325.assignment01.concert.common.jackson.LocalDateTimeSerializer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "SEATS")
public class Seat {

    // TODO Implement this class.

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String label;

	private boolean	isBooked;

	@Column
	private LocalDateTime date;
	private BigDecimal price;
//	@ManyToOne
//	private Booking booking;

	public Seat() {}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
