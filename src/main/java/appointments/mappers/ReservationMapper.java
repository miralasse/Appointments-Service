package appointments.mappers;

import appointments.domain.Reservation;
import appointments.dto.ReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mappings({
            @Mapping(source = "schedule.id", target = "scheduleId"),
            @Mapping(source = "service.id", target = "serviceId"),
            @Mapping(source = "child.id", target = "childId")
    })
    ReservationDTO reservationToReservationDTO(Reservation entity);


    @Mappings({
            @Mapping(source = "scheduleId", target = "schedule.id"),
            @Mapping(source = "serviceId", target = "service.id"),
            @Mapping(source = "childId", target = "child.id")
    })
    Reservation reservationDTOToReservation(ReservationDTO dto);

    List<Reservation> reservationDTOListToReservationList(List<ReservationDTO> list);

    List<ReservationDTO> reservationListToReservationDTOList(List<Reservation> list);

}
