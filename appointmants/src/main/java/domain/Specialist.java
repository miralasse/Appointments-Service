package main.java.domain;

import java.util.Objects;

public class Specialist {
    private Integer id;    //использован Integer вместо int, чтобы не было значения по-умолчанию 0
    private String position;
    private Integer roomNumber;
    private boolean active;
    private Organization organization;

    public Specialist() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Specialist)) return false;
        Specialist that = (Specialist) o;
        return isActive() == that.isActive() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getPosition(), that.getPosition()) &&
                Objects.equals(getRoomNumber(), that.getRoomNumber()) &&
                Objects.equals(getOrganization(), that.getOrganization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPosition(), getRoomNumber(), isActive(), getOrganization());
    }
}
