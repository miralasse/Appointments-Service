package appointments.mappers;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.dto.ScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;

import static appointments.utils.Constants.RESERVATION_EMPTY_SERVICE_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SERVICES_MESSAGE;
import static java.util.stream.Collectors.toList;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mappings({
            @Mapping(source = "specialist.id", target = "specialistId"),
            @Mapping(source = "services", target = "serviceIds"),
            @Mapping(source = "reservations", target = "reservationIds")
    })
    ScheduleDTO scheduleToScheduleDto(Schedule entity);


    @Mappings({
            @Mapping(source = "specialistId", target = "specialist.id"),
            @Mapping(source = "serviceIds", target = "services"),
            @Mapping(source = "reservationIds", target = "reservations")
    })
    Schedule scheduleDTOToSchedule(ScheduleDTO dto);


    List<Schedule> scheduleDTOListToScheduleList(List<ScheduleDTO> list);


    List<ScheduleDTO> scheduleListToScheduleDTOList(List<Schedule> list);


    default List<Integer> servicesToIds(List<Service> services) {

        if (services == null) {
            throw new IllegalArgumentException(RESERVATION_EMPTY_SERVICE_MESSAGE);
        }

        return services
                .stream()
                .map(Service::getId)
                .collect(toList());
    }


    default List<Service> idsToServices(List<Integer> serviceIds) {

        if (serviceIds == null) {
            throw new IllegalArgumentException(SCHEDULE_EMPTY_SERVICES_MESSAGE);
        }

        return serviceIds
                .stream()
                .map(id -> new Service(id, null, false))
                .collect(toList());
    }


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
