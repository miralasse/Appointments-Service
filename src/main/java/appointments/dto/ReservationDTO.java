package appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static appointments.utils.Constants.RESERVATION_EMPTY_CHILD_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_SCHEDULE_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_SERVICE_MESSAGE;
import static appointments.utils.Constants.RESERVATION_INCORRECT_DATETIME_MESSAGE;


/**
 * @author yanchenko_evgeniya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;

    /** Дата и время начала приема для этого талона */
    @NotNull(message = RESERVATION_INCORRECT_DATETIME_MESSAGE)
    private LocalDateTime dateTime;


    /** Идентификатор расписания (объект Schedule), в котором осуществляется эта запись на прием */
    @NotNull(message = RESERVATION_EMPTY_SCHEDULE_MESSAGE)
    private Long scheduleId;


    /** Идентификатор услуги (объект Service), которая выбрана как цель обращения при бронировании времени */
    @NotNull(message = RESERVATION_EMPTY_SERVICE_MESSAGE)
    private Integer serviceId;


    /** Флаг активности */
    private boolean active;


    /** Идентификатор ребёнка (объект Child), в интересах которого осуществляется эта запись на прием */
    @NotNull(message = RESERVATION_EMPTY_CHILD_MESSAGE)
    private Integer childId;

}
