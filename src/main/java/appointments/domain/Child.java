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
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.CHILD_MAX_BIRTH_CERTIFICATE_NUMBER;
import static appointments.utils.Constants.CHILD_MAX_BIRTH_CERTIFICATE_SERIES_LENGTH;
import static appointments.utils.Constants.CHILD_MAX_NAME_LENGTH;
import static appointments.utils.Constants.CHILD_MIN_BIRTH_CERTIFICATE_SERIES_LENGTH;
import static appointments.utils.Constants.CHILD_MIN_NAME_LENGTH;
import static appointments.utils.Constants.CHILD_NULL_EMAIL_MESSAGE;
import static appointments.utils.Constants.CHILD_NULL_FIRST_NAME_MESSAGE;
import static appointments.utils.Constants.CHILD_NULL_LAST_NAME_MESSAGE;
import static appointments.utils.Constants.CHILD_NULL_NUMBER_MESSAGE;
import static appointments.utils.Constants.CHILD_NULL_PHONE_MESSAGE;
import static appointments.utils.Constants.CHILD_NULL_SERIES_MESSAGE;
import static appointments.utils.Constants.CHILD_WRONG_FIRST_NAME_LENGTH;
import static appointments.utils.Constants.CHILD_WRONG_LAST_NAME_LENGTH;
import static appointments.utils.Constants.CHILD_WRONG_NUMBER_LENGTH;
import static appointments.utils.Constants.CHILD_WRONG_PHONE_MESSAGE;
import static appointments.utils.Constants.CHILD_WRONG_SERIES_LENGTH;


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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    /** Серия свидетельства о рождении */
    @NotEmpty(message = CHILD_NULL_SERIES_MESSAGE)
    @Size(
            min = CHILD_MIN_BIRTH_CERTIFICATE_SERIES_LENGTH,
            max = CHILD_MAX_BIRTH_CERTIFICATE_SERIES_LENGTH,
            message = CHILD_WRONG_SERIES_LENGTH
    )
    @Column(name = "birth_certificate_series")
    private String birthCertificateSeries;


    /** Номер свидетельства о рождении */
    @NotNull(message = CHILD_NULL_NUMBER_MESSAGE)
    @Max(
            value = CHILD_MAX_BIRTH_CERTIFICATE_NUMBER,
            message = CHILD_WRONG_NUMBER_LENGTH
    )
    @Column(name = "birth_certificate_number")
    private Integer birthCertificateNumber;


    /** Контактный номер телефона */
    @NotEmpty(message = CHILD_NULL_PHONE_MESSAGE)
    @Pattern(
            regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = CHILD_WRONG_PHONE_MESSAGE
    )
    @Column(name = "phone_number")
    private String phoneNumber;


    /** Электронная почта */
    @NotEmpty(message = CHILD_NULL_EMAIL_MESSAGE)
    @Email
    @Column
    private String email;


    /** Фамилия ребёнка */
    @NotEmpty(message = CHILD_NULL_LAST_NAME_MESSAGE)
    @Size(
            min = CHILD_MIN_NAME_LENGTH,
            max = CHILD_MAX_NAME_LENGTH,
            message = CHILD_WRONG_LAST_NAME_LENGTH
    )
    @Column(name = "last_name")
    private String lastName;


    /** Имя ребёнка */
    @NotEmpty(message = CHILD_NULL_FIRST_NAME_MESSAGE)
    @Size(
            min = CHILD_MIN_NAME_LENGTH,
            max = CHILD_MAX_NAME_LENGTH,
            message = CHILD_WRONG_FIRST_NAME_LENGTH
    )
    @Column(name = "first_name")
    private String firstName;


    /** Отчество ребёнка */
    @Column
    private String patronymic;

}
