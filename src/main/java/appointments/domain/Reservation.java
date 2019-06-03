package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Reservation {

    private Long id;

    /** Поле даты и времени начала приема для этого талона */
    private LocalDateTime dateTime;

    /** Поле Ссылка на расписание (объект Schedule), в котором осуществляется эта запись на прием */
    private Schedule schedule;

    /** Поле Ссылка на услугу (объект Service), которая выбрана как цель обращения при бронировании времени */
    private Service service;

    /** Поле Флаг активности */
    private boolean active;

    /** Поле Ссылка на ребёнка (объект Child), в интересах которого осуществлена эта запись на прием */
    private Child child;

}
