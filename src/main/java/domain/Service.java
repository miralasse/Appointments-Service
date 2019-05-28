package main.java.domain;

import java.util.Objects;

/**
 * Класс, описывающий сущность Услуга (Цель обращения).
 * Содержит название и флаг активности услуги.
 */
public class Service {

    private Integer id;

    /** Поле Название */
    private String name;

    /** Поле Флаг активности */
    private boolean active;


    public Service() {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        Service service = (Service) o;
        return isActive() == service.isActive() &&
                Objects.equals(getId(), service.getId()) &&
                Objects.equals(getName(), service.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), isActive());
    }
}
