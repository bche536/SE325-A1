package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;

public class ConcertMapper {

    static Concert toDomainModel(ConcertDTO ConcertDTO) {
        return new Concert(ConcertDTO.getId(),
                ConcertDTO.getTitle(),
                ConcertDTO.getImageName(),
                ConcertDTO.getBlurb());
    }

    public static ConcertDTO toDto(Concert concert) {
        return new ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlurb());

    }

    public static ConcertSummaryDTO toConcertSummaryDto(Concert concert) {
        return new ConcertSummaryDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName());
    }
}
