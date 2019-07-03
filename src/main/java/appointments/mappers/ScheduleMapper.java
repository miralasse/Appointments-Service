package appointments.mappers;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.dto.ScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mappings({
            @Mapping(source = "reservations", target = "reservationIds"),
            @Mapping(source = "intervalOfReception", target = "interval")
    })
    ScheduleDTO scheduleToScheduleDto(Schedule entity);


    @Mappings({
            @Mapping(source = "reservationIds", target = "reservations"),
            @Mapping(source = "interval", target = "intervalOfReception")
    })
    Schedule scheduleDTOToSchedule(ScheduleDTO dto);


    default Page<ScheduleDTO> schedulePageToScheduleDTOPage(Page<Schedule> page) {
        return page.map(this::scheduleToScheduleDto);
    }

    List<Schedule> scheduleDTOListToScheduleList(List<ScheduleDTO> list);


    List<ScheduleDTO> scheduleListToScheduleDTOList(List<Schedule> list);


    default List<Long> reservationsToIds(List<Reservation> reservations) {

        if (reservations == null) {
            reservations = new ArrayList<>();
        }

        return reservations
                .stream()
                .map(Reservation::getId)
                .collect(toList());
    }


    default List<Reservation> idsToReservations(List<Long> reservationIds) {

        if (reservationIds == null) {
            reservationIds = new ArrayList<>();
        }

        return reservationIds
                .stream()
                .map(id -> new Reservation(id, null, null, null, false, null))
                .collect(toList());
    }
}
