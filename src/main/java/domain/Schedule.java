package main.java.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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
 */
public class Schedule {

    private Long id;

    /** Поле Ссылка на специалиста, к которому относится это расписание приема */
    private Specialist specialist;

    /** Поле Дата, на которую формируется это расписание */
    private LocalDate date;

    /** Поле Список услуг (ссылки на объекты Услуг), которые доступны в списке выбора при бронировании времени в этом расписании */
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


    public Schedule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Duration getInterval() {
        return interval;
    }

    public void setInterval(Duration interval) {
        this.interval = interval;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return isActive() == schedule.isActive() &&
                Objects.equals(getId(), schedule.getId()) &&
                Objects.equals(getSpecialist(), schedule.getSpecialist()) &&
                Objects.equals(getDate(), schedule.getDate()) &&
                Objects.equals(getServices(), schedule.getServices()) &&
                Objects.equals(getStartTime(), schedule.getStartTime()) &&
                Objects.equals(getEndTime(), schedule.getEndTime()) &&
                Objects.equals(getInterval(), schedule.getInterval()) &&
                Objects.equals(getReservations(), schedule.getReservations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSpecialist(), getDate(), getServices(), getStartTime(), getEndTime(), getInterval(), getReservations(), isActive());
    }
}
