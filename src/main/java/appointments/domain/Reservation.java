package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static appointments.utils.Constants.RESERVATION_EMPTY_CHILD_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_SCHEDULE_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_SERVICE_MESSAGE;
import static appointments.utils.Constants.RESERVATION_INCORRECT_DATETIME_MESSAGE;


/**
 * Класс, описывающий сущность Запись на прием (Бронь).
 * Содержит:
 * - дату и время начала приема для этого талона
 * - ссылку на расписание (объект Schedule), в котором осуществляется эта запись на прием
 * - ссылку на услугу (объект Service), которая выбрана как цель обращения при бронировании времени
 * - флаг активности
 * - ссылку на ребёнка (объект Child), в интересах которого осуществлена эта запись на прием
 *
 * @author yanchenko_evgeniya
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    /** Дата и время начала приема для этого талона */
    @NotNull(message = RESERVATION_INCORRECT_DATETIME_MESSAGE)
    @Column(name = "date_time")
    private LocalDateTime dateTime;


    /** Ссылка на расписание (объект Schedule), в котором осуществляется эта запись на прием */
    @NotNull(message = RESERVATION_EMPTY_SCHEDULE_MESSAGE)
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;


    /** Ссылка на услугу (объект Service), которая выбрана как цель обращения при бронировании времени */
    @NotNull(message = RESERVATION_EMPTY_SERVICE_MESSAGE)
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;


    /** Флаг активности */
    @Column
    private boolean active;


    /** Ссылка на ребёнка (объект Child), в интересах которого осуществлена эта запись на прием */
    @NotNull(message = RESERVATION_EMPTY_CHILD_MESSAGE)
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

}
