package main.java.domain;

import java.util.Objects;

/**
 * Класс, описывающий сущность Специалист (сотрудник, ведущий прием).
 * Содержит имя (ФИО/должность), номер кабинета, флаг активности и ссылку на организацию, к которой относится специалист.
 */
public class Specialist {

    private Integer id;

    /** Поле для указания должности или ФИО специалиста для отображения на сайте */
    private String name;

    /** Поле Номер кабинета */
    private Integer roomNumber;

    /** Поле Флаг активности специалиста */
    private boolean active;

    /** Поле Ссылка на организацию (объект Organization), к которой относится специалист */
    private Organization organization;


    public Specialist() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getRoomNumber(), that.getRoomNumber()) &&
                Objects.equals(getOrganization(), that.getOrganization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRoomNumber(), isActive(), getOrganization());
    }
}
