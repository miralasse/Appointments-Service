package main.java.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private Integer id;
    private LocalDateTime dateTime;
    private Schedule schedule;
    private Service service;
    private boolean active;
    private Child child;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        Reservation that = (Reservation) o;
        return isActive() == that.isActive() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDateTime(), that.getDateTime()) &&
                Objects.equals(getSchedule(), that.getSchedule()) &&
                Objects.equals(getService(), that.getService()) &&
                Objects.equals(getChild(), that.getChild());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateTime(), getSchedule(), getService(), isActive(), getChild());
    }
}
