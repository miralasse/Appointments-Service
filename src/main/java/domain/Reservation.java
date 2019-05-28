package domain;

import java.time.LocalDateTime;
import java.util.Objects;

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


    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation)o;
        return isActive() == that.isActive()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getDateTime(), that.getDateTime())
                && Objects.equals(getSchedule(), that.getSchedule())
                && Objects.equals(getService(), that.getService())
                && Objects.equals(getChild(), that.getChild());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateTime(), getSchedule(),
                getService(), isActive(), getChild());
    }
}
