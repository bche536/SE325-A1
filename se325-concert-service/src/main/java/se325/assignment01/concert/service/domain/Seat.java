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
	@Column(name = "ID", nullable = false, length = 255)
	private Long id;

	@Column(name = "LABEL", nullable = false, length = 255)
	private String label;

	@Column(name = "IS_BOOKED", nullable = false, length = 255)
	private boolean	isBooked;

	@Column(name = "DATE", nullable = false, length = 255)
	private LocalDateTime date;

	@Column(name = "PRICE", nullable = false, length = 255)
	private BigDecimal price;
//	@ManyToOne
//	private Booking booking;

	public Seat() {}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean booked) {
		isBooked = booked;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}



}
