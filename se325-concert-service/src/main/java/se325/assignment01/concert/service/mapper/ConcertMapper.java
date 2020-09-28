package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.service.domain.Concert;
import se325.assignment01.concert.service.domain.Performer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConcertMapper {

    static Concert toDomainModel(ConcertDTO ConcertDTO) {
        return new Concert(ConcertDTO.getId(),
                ConcertDTO.getTitle(),
                ConcertDTO.getImageName(),
                ConcertDTO.getBlurb());
    }

    public static ConcertDTO toDto(Concert concert) {
        ConcertDTO dtoConcert = new ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlurb());

        List<LocalDateTime> datesList= new ArrayList<>();
        datesList.addAll(concert.getDates());
        dtoConcert.setDates(datesList);

        List<PerformerDTO> performerDTOList = new ArrayList<>();
        for (Performer p : concert.getPerformers()) {
            performerDTOList.add(PerformerMapper.toDto(p));
        }
        dtoConcert.setPerformers(performerDTOList);
        return dtoConcert;
    }


    public static ConcertSummaryDTO toConcertSummaryDto(Concert concert) {
        return new ConcertSummaryDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName());
    }
}
