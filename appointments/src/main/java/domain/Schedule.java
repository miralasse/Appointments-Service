package main.java.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Schedule {

    /* здесь не использую id, т.к. предполагаю сделать первичный ключ составным специалист + дата,
    чтобы нельзя было добавить несколько расписаний для одного специалиста на одну дату */

    private Specialist specialist;
    private LocalDate date;
    private List<Service> services;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration interval;
    private List<Reservation> reservations;
    private boolean active;

    public Schedule() {
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
        return Objects.hash(getSpecialist(), getDate(), getServices(), getStartTime(), getEndTime(), getInterval(), getReservations(), isActive());
    }
}
