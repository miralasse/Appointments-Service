package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
import static appointments.utils.Constants.SCHEDULE_EMPTY_ROOM_NUMBER_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_MAX_ROOM_NUMBER_LENGTH;
import static appointments.utils.Constants.SCHEDULE_MIN_ROOM_NUMBER_LENGTH;
import static appointments.utils.Constants.SCHEDULE_WRONG_ROOM_NUMBER_LENGTH;


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
@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "reservations")
public class Schedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;


    /** Ссылка на специалиста, к которому относится это расписание приема */
    @NotNull(message = SCHEDULE_EMPTY_SPECIALIST_MESSAGE)
    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private Specialist specialist;

    /** Номер кабинета */
    @NotEmpty(message = SCHEDULE_EMPTY_ROOM_NUMBER_MESSAGE)
    @Size(
            min = SCHEDULE_MIN_ROOM_NUMBER_LENGTH,
            max = SCHEDULE_MAX_ROOM_NUMBER_LENGTH,
            message = SCHEDULE_WRONG_ROOM_NUMBER_LENGTH
    )
    @Column(name = "room_number")
    private String roomNumber;


    /** Дата, на которую формируется это расписание */
    @NotNull(message =  SCHEDULE_INCORRECT_DATE_MESSAGE)
    @Column(name = "date")
    private LocalDate date;


    /** Список услуг (ссылки на объекты Услуг),
     * которые доступны в списке выбора при бронировании времени в этом расписании */
    @NotNull(message = SCHEDULE_EMPTY_SERVICES_MESSAGE)
    @ManyToMany
    @JoinTable(
            name = "services_schedules",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;


    /** Время начала приема */
    @NotNull(message = SCHEDULE_EMPTY_START_TIME_MESSAGE)
    @Column(name = "start_time")
    private LocalTime startTime;


    /** Время окончания приема */
    @NotNull(message = SCHEDULE_EMPTY_END_TIME_MESSAGE)
    @Column(name = "end_time")
    private LocalTime endTime;


    /** Длительность/Интервал приема одного талона в минутах*/
    @NotNull(message = SCHEDULE_EMPTY_INTERVAL_MESSAGE)
    @Range(
            min = SCHEDULE_MIN_INTERVAL_LENGTH,
            max = SCHEDULE_MAX_INTERVAL_LENGTH,
            message = SCHEDULE_WRONG_INTERVAL_LENGTH
    )
    @Column(name = "interval_of_reception")
    private Integer intervalOfReception;


    /** Список забронированных интервалов времени (ссылки на объекты Reservation) в этом расписании */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

}
