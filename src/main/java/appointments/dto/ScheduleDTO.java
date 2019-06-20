package appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static appointments.utils.Constants.SCHEDULE_EMPTY_END_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_INTERVAL_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SERVICES_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SPECIALIST_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_START_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_INCORRECT_DATE_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_MAX_INTERVAL_LENGTH;
import static appointments.utils.Constants.SCHEDULE_MIN_INTERVAL_LENGTH;
import static appointments.utils.Constants.SCHEDULE_WRONG_INTERVAL_LENGTH;


/**
 * @author yanchenko_evgeniya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "reservationIds")
public class ScheduleDTO {

    /** Индентификатора расписания: null для нового, not null для существующего */
    private Long id;


    /** Поле Ссылка на специалиста, к которому относится это расписание приема */
    @NotNull(message = SCHEDULE_EMPTY_SPECIALIST_MESSAGE)
    private Integer specialistId;


    /** Поле Дата, на которую формируется это расписание */
    @NotNull(message = SCHEDULE_INCORRECT_DATE_MESSAGE)
    private LocalDate date;


    /** Поле Список услуг (ссылки на объекты Услуг),
     * которые доступны в списке выбора при бронировании времени в этом расписании */
    @NotNull(message = SCHEDULE_EMPTY_SERVICES_MESSAGE)
    private List<Integer> serviceIds;


    /** Поле Время начала приема */
    @NotNull(message = SCHEDULE_EMPTY_START_TIME_MESSAGE)
    private LocalTime startTime;


    /** Поле Время окончания приема */
    @NotNull(message = SCHEDULE_EMPTY_END_TIME_MESSAGE)
    private LocalTime endTime;


    /** Поле Длительность/Интервал приема одного талона в минутах*/
    @NotNull(message = SCHEDULE_EMPTY_INTERVAL_MESSAGE)
    @Range(
            min = SCHEDULE_MIN_INTERVAL_LENGTH,
            max = SCHEDULE_MAX_INTERVAL_LENGTH,
            message = SCHEDULE_WRONG_INTERVAL_LENGTH
    )
    private Integer intervalOfReception;


    /** Поле Список забронированных интервалов времени (ссылки на объекты Reservation) в этом расписании */
    private List<Long> reservationIds;


    /** Поле Флаг активности этого расписания */
    private boolean active;

}
