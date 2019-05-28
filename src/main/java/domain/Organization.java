package main.java.domain;

import java.util.Objects;

/**
 * Класс, описывающий сущность Организация.
 * Содержит название, фактический адрес и описание с контактной информацией.
 */
public class Organization {

    private Integer id;

    /** Поле Название */
    private String name;

    /** Поле Фактический адрес */
    private String actualAddress;

    /** Поле Описание для внесения контактной и другой полезной информации */
    private String description;


    public Organization() {
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

    public String getActualAddress() {
        return actualAddress;
    }

    public void setActualAddress(String actualAddress) {
        this.actualAddress = actualAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Organization)) return false;
        Organization that = (Organization) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getActualAddress(), that.getActualAddress()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getActualAddress(), getDescription());
    }
}
