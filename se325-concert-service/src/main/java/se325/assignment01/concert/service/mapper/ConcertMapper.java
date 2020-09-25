package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;

public class ConcertMapper {

    static Concert toDomainModel(ConcertDTO ConcertDTO) {
        Concert fullConcert;
        fullConcert = new Concert(ConcertDTO.getId(),
                ConcertDTO.getTitle(),
                ConcertDTO.getImageName(),
                ConcertDTO.getBlurb());
        return fullConcert;
    }

    public static ConcertDTO toDto(Concert concert) {
        ConcertDTO dtoConcert =
                new ConcertDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName(),
                        concert.getBlurb());
        return dtoConcert;

    }

    public static ConcertSummaryDTO toConcertSummaryDto(Concert concert) {
        ConcertSummaryDTO dtoConcertSummary =
                new ConcertSummaryDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName());
        return dtoConcertSummary;

    }
}
