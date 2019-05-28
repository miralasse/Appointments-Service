package main.java.domain;

import java.util.Objects;

public class Child {
    private Long birthCertificateNumber;
    private String phoneNumber;
    private String email;
    /*
    для текущей задачи поля ФИО не указаны, но вероятнее всего они нужны в дальнейшем
     */
    private String lastName;
    private String firstName;
    private String patronymic;

    public Long getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(Long birthCertificateNumber) {
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
        Child child = (Child) o;
        return Objects.equals(getBirthCertificateNumber(), child.getBirthCertificateNumber()) &&
                Objects.equals(getPhoneNumber(), child.getPhoneNumber()) &&
                Objects.equals(getEmail(), child.getEmail()) &&
                Objects.equals(getLastName(), child.getLastName()) &&
                Objects.equals(getFirstName(), child.getFirstName()) &&
                Objects.equals(getPatronymic(), child.getPatronymic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBirthCertificateNumber(), getPhoneNumber(), getEmail(), getLastName(), getFirstName(), getPatronymic());
    }
}
