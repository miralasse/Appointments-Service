package domain;

import java.util.Objects;

/**
 * Класс, описывающий сущность Ребёнок.
 * Содержит серию и номер свидельства о рождении, ФИО и контактную информацию
 *
 * @author yanchenko_evgeniya
 */
public class Child {

    private Integer id;

    /** Поле Серия свидетельства о рождении */
    private String birthCertificateSeries;

    /** Поле Номер свидетельства о рождении */
    private Integer birthCertificateNumber;

    /** Поле Контактный номер телефона */
    private String phoneNumber;

    /** Поле Электронная почта */
    private String email;

    /** Поле Фамилия ребёнка */
    private String lastName;

    /** Поле Имя ребёнка */
    private String firstName;

    /** Поле Отчество ребёнка */
    private String patronymic;


    public Child() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBirthCertificateSeries() {
        return birthCertificateSeries;
    }

    public void setBirthCertificateSeries(String birthCertificateSeries) {
        this.birthCertificateSeries = birthCertificateSeries;
    }

    public Integer getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(Integer birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Child)) return false;
        Child child = (Child)o;
        return Objects.equals(getId(), child.getId())
                && Objects.equals(getBirthCertificateSeries(), child.getBirthCertificateSeries())
                && Objects.equals(getBirthCertificateNumber(), child.getBirthCertificateNumber())
                && Objects.equals(getPhoneNumber(), child.getPhoneNumber())
                && Objects.equals(getEmail(), child.getEmail())
                && Objects.equals(getLastName(), child.getLastName())
                && Objects.equals(getFirstName(), child.getFirstName())
                && Objects.equals(getPatronymic(), child.getPatronymic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBirthCertificateSeries(), getBirthCertificateNumber(),
                getPhoneNumber(), getEmail(), getLastName(), getFirstName(), getPatronymic());
    }
}
