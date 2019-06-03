package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Класс, описывающий сущность Расписание.
 * Содержит:
 * - ссылку на специалиста, к которому относится это расписание;
 * - дату, на которую формируется это расписание;
 * - список услуг (объекты Service), которые доступны в списке выбора при бронировании времени в этом расписании
 * - время начала приема
 * - время окончания приема
 * - длительность приема одного талона
 * - список забронированных интервалов времени (записей на прием - объекты Reservation) в этом расписании
 * - флаг активности
 *
 * @author yanchenko_evgeniya
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "reservations")
public class Schedule {

    private Long id;

    /** Поле Ссылка на специалиста, к которому относится это расписание приема */
    private Specialist specialist;

    /** Поле Дата, на которую формируется это расписание */
    private LocalDate date;

    /** Поле Список услуг (ссылки на объекты Услуг),
     * которые доступны в списке выбора при бронировании времени в этом расписании */
    private List<Service> services;

    /** Поле Время начала приема */
    private LocalTime startTime;

    /** Поле Время окончания приема */
    private LocalTime endTime;

    /** Поле Длительность/Интервал приема одного талона */
    private Duration interval;

    /** Поле Список забронированных интервалов времени (ссылки на объекты Reservation) в этом расписании */
    private List<Reservation> reservations;

    /** Поле Флаг активности этого расписания */
    private boolean active;

}
