package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Класс, описывающий сущность Ребёнок.
 * Содержит серию и номер свидельства о рождении, ФИО и контактную информацию
 *
 * @author yanchenko_evgeniya
 */
@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Child {

    private static final int MIN_BIRTH_CERTIFICATE_SERIES_LENGTH = 3;
    private static final int MAX_BIRTH_CERTIFICATE_SERIES_LENGTH = 6;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    /** Поле Серия свидетельства о рождении */
    @NotNull(message = "Серия свидетельства о рождении должна быть указана")
    @Size(
            min = MIN_BIRTH_CERTIFICATE_SERIES_LENGTH,
            max = MAX_BIRTH_CERTIFICATE_SERIES_LENGTH,
            message = "Длина серии свидетельства о рождении должна быть от "
                    + MIN_BIRTH_CERTIFICATE_SERIES_LENGTH
                    + " до "
                    + MAX_BIRTH_CERTIFICATE_SERIES_LENGTH
                    + " символов"
    )
    @Column(name = "birth_certificate_series")
    private String birthCertificateSeries;


    /** Поле Номер свидетельства о рождении */
    @NotNull(message = "Номер свидетельства о рождении должен быть указан")
    @Pattern(regexp = "\\d{6}",
            message = "Номер свидетельства о рождении должен содержать только 6 цифр")
    @Column(name = "birth_certificate_number")
    private Integer birthCertificateNumber;


    /** Поле Контактный номер телефона */
    @NotNull(message = "Контактный телефон должен быть указан")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "Номер телефона должен начинаться с кода 8 или +7 и далее содержать 10 цифр")
    @Column(name = "phone_number")
    private String phoneNumber;


    /** Поле Электронная почта */
    @NotNull(message = "Адрес электронной почты должен быть указан")
    @Email
    @Column
    private String email;


    /** Поле Фамилия ребёнка */
    @NotNull(message = "Фамилия должна быть указана")
    @Size(
            min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Длина фамилии должна быть от "
                    + MIN_NAME_LENGTH
                    + " до "
                    + MAX_NAME_LENGTH
                    + " символов"
    )
    @Column(name = "last_name")
    private String lastName;


    /** Поле Имя ребёнка */
    @NotNull(message = "Имя должно быть указана")
    @Size(
            min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Длина имени должна быть от "
                    + MIN_NAME_LENGTH
                    + " до "
                    + MAX_NAME_LENGTH
                    + " символов"
    )
    @Column(name = "first_name")
    private String firstName;


    /** Поле Отчество ребёнка */
    @Column
    private String patronymic;

}
