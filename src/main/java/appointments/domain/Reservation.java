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

    /** Поле даты и времени начала приема для этого талона */
    @NotNull(message = "Дата и время начала приема должны быть указаны")
    @Column(name = "date_time")
    private LocalDateTime dateTime;


    /** Поле Ссылка на расписание (объект Schedule), в котором осуществляется эта запись на прием */
    @NotNull(message = "Для записи на прием нужно указать расписание")
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;


    /** Поле Ссылка на услугу (объект Service), которая выбрана как цель обращения при бронировании времени */
    @NotNull(message = "Для записи на прием должна быть выбрана услуга")
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;


    /** Поле Флаг активности */
    @Column
    private boolean active;


    /** Поле Ссылка на ребёнка (объект Child), в интересах которого осуществлена эта запись на прием */
    @NotNull(message = "Для записи на прием нужно указать ребёнка")
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

}
