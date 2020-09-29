package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.BookingDTO;
import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Booking;
import se325.assignment01.concert.service.domain.Performer;
import se325.assignment01.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public static BookingDTO toDto (Booking booking) {
        List<SeatDTO> dtoSeatList = new ArrayList<>();
        for (Seat s : booking.getSeats()) {
            dtoSeatList.add(SeatMapper.toDto(s));
        }

        return new BookingDTO(
                booking.getConcertId(),
                booking.getDate(),
                dtoSeatList);
    }
}
