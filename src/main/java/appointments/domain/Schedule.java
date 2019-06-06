package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "reservations")
public class Schedule {

    private static final int MIN_INTERVAL_LENGTH = 5;
    private static final int MAX_INTERVAL_LENGTH = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;


    /** Поле Ссылка на специалиста, к которому относится это расписание приема */
    @NotNull(message = "Для расписания должен быть указан специалист")
    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private Specialist specialist;


    /** Поле Дата, на которую формируется это расписание */
    @NotNull(message = "Для расписания должна быть указана дата")
    @Column(name = "date")
    private LocalDate date;


    /** Поле Список услуг (ссылки на объекты Услуг),
     * которые доступны в списке выбора при бронировании времени в этом расписании */
    @NotNull(message = "Для расписания должен быть указаны услуги (цели обращения)")
    @ManyToMany
    @JoinTable(name = "services_schedules",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services;


    /** Поле Время начала приема */
    @NotNull(message = "Для расписания должно быть указано время начала приема")
    @Column(name = "start_time")
    private LocalTime startTime;


    /** Поле Время окончания приема */
    @NotNull(message = "Для расписания должно быть указано время окончания приема")
    @Column(name = "end_time")
    private LocalTime endTime;


    /** Поле Длительность/Интервал приема одного талона в минутах*/
    @NotNull(message = "Для расписания должен быть указан интервал приема (время на один талон) в минутах")
    @Size(
            min = MIN_INTERVAL_LENGTH,
            max = MAX_INTERVAL_LENGTH,
            message = "Интервал приема должен быть от "
                    + MIN_INTERVAL_LENGTH
                    + " до "
                    + MAX_INTERVAL_LENGTH
                    + " минут"
    )
    @Column(name = "interval_of_reception")
    private Integer intervalOfReception;


    /** Поле Список забронированных интервалов времени (ссылки на объекты Reservation) в этом расписании */
    @OneToMany(mappedBy = "schedule")
    private List<Reservation> reservations;


    /** Поле Флаг активности этого расписания */
    @Column
    private boolean active;

}
