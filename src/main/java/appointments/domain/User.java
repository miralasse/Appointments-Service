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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.USER_EMPTY_EMAIL_MESSAGE;
import static appointments.utils.Constants.USER_EMPTY_FIRST_NAME_MESSAGE;
import static appointments.utils.Constants.USER_EMPTY_LAST_NAME_MESSAGE;
import static appointments.utils.Constants.USER_EMPTY_PASSWORD_MESSAGE;
import static appointments.utils.Constants.USER_EMPTY_PHONE_MESSAGE;
import static appointments.utils.Constants.USER_EMPTY_USERNAME_MESSAGE;
import static appointments.utils.Constants.USER_MAX_NAME_LENGTH;
import static appointments.utils.Constants.USER_MIN_NAME_LENGTH;
import static appointments.utils.Constants.USER_WRONG_FIRST_NAME_LENGTH;
import static appointments.utils.Constants.USER_WRONG_LAST_NAME_LENGTH;
import static appointments.utils.Constants.USER_WRONG_PHONE_MESSAGE;
import static appointments.utils.Constants.USER_WRONG_USERNAME_LENGTH;

/**
 * Класс, описывающий сущность Пользователь.
 * Содержит уникальное имя пользователя, пароль, фамилию, имя и контактные данные.
 *
 * @author yanchenko_evgeniya
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    /** Имя учетной записи */
    @NotEmpty(message = USER_EMPTY_USERNAME_MESSAGE)
    @Size(
            min = USER_MIN_NAME_LENGTH,
            max = USER_MAX_NAME_LENGTH,
            message = USER_WRONG_USERNAME_LENGTH
    )
    @Column
    private String username;


    /** Пароль пользователя */
    @NotEmpty(message = USER_EMPTY_PASSWORD_MESSAGE)
    @Column
    private String password;


    /** Имя пользователя */
    @NotEmpty(message = USER_EMPTY_FIRST_NAME_MESSAGE)
    @Size(
            min = USER_MIN_NAME_LENGTH,
            max = USER_MAX_NAME_LENGTH,
            message = USER_WRONG_FIRST_NAME_LENGTH
    )
    @Column(name = "first_name")
    private String firstName;


    /** Фамилия пользователя */
    @NotEmpty(message = USER_EMPTY_LAST_NAME_MESSAGE)
    @Size(
            min = USER_MIN_NAME_LENGTH,
            max = USER_MAX_NAME_LENGTH,
            message = USER_WRONG_LAST_NAME_LENGTH
    )
    @Column(name = "last_name")
    private String lastName;


    /** Электронная почта */
    @NotEmpty(message = USER_EMPTY_EMAIL_MESSAGE)
    @Email
    @Column
    private String email;


    /** Контактный номер телефона */
    @NotEmpty(message = USER_EMPTY_PHONE_MESSAGE)
    @Pattern(
            regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = USER_WRONG_PHONE_MESSAGE
    )
    @Column(name = "phone_number")
    private String phoneNumber;

    /** Ссылка на роль, которой обладает пользователь */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
