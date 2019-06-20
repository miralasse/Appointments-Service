package appointments.dto;

import appointments.validation.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
import static appointments.utils.Constants.USER_MAX_PASSWORD_LENGTH;
import static appointments.utils.Constants.USER_MIN_NAME_LENGTH;
import static appointments.utils.Constants.USER_MIN_PASSWORD_LENGTH;
import static appointments.utils.Constants.USER_PASSWORDS_DO_NOT_MATCH;
import static appointments.utils.Constants.USER_WRONG_FIRST_NAME_LENGTH;
import static appointments.utils.Constants.USER_WRONG_LAST_NAME_LENGTH;
import static appointments.utils.Constants.USER_WRONG_PASSWORD_LENGTH;
import static appointments.utils.Constants.USER_WRONG_PHONE_MESSAGE;
import static appointments.utils.Constants.USER_WRONG_USERNAME_LENGTH;

/**
 * @author yanchenko_evgeniya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = USER_PASSWORDS_DO_NOT_MATCH)
public class UserDTO {

    /** Индентификатора пользователя: null для нового, not null для существующего */
    private Integer id;

    /** Имя учетной записи */
    @NotEmpty(message = USER_EMPTY_USERNAME_MESSAGE)
    @Size(
            min = USER_MIN_NAME_LENGTH,
            max = USER_MAX_NAME_LENGTH,
            message = USER_WRONG_USERNAME_LENGTH
    )
    private String username;


    /** Пароль пользователя */
    @NotEmpty(message = USER_EMPTY_PASSWORD_MESSAGE)
    @Size(
            min = USER_MIN_PASSWORD_LENGTH,
            max = USER_MAX_PASSWORD_LENGTH,
            message = USER_WRONG_PASSWORD_LENGTH
    )
    private String password;

    /** Подтверждение пароля пользователя */
    @NotEmpty(message = USER_EMPTY_PASSWORD_MESSAGE)
    @Size(
            min = USER_MIN_PASSWORD_LENGTH,
            max = USER_MAX_PASSWORD_LENGTH,
            message = USER_WRONG_PASSWORD_LENGTH
    )
    private String matchingPassword;

    /** Имя пользователя */
    @NotEmpty(message = USER_EMPTY_FIRST_NAME_MESSAGE)
    @Size(
            min = USER_MIN_NAME_LENGTH,
            max = USER_MAX_NAME_LENGTH,
            message = USER_WRONG_FIRST_NAME_LENGTH
    )
    private String firstName;


    /** Фамилия пользователя */
    @NotEmpty(message = USER_EMPTY_LAST_NAME_MESSAGE)
    @Size(
            min = USER_MIN_NAME_LENGTH,
            max = USER_MAX_NAME_LENGTH,
            message = USER_WRONG_LAST_NAME_LENGTH
    )
    private String lastName;


    /** Электронная почта */
    @NotEmpty(message = USER_EMPTY_EMAIL_MESSAGE)
    @Email
    private String email;


    /** Контактный номер телефона */
    @NotEmpty(message = USER_EMPTY_PHONE_MESSAGE)
    @Pattern(
            regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = USER_WRONG_PHONE_MESSAGE
    )
    private String phoneNumber;

    /** Название роли, которой обладает пользователь */
    private String roleName;

}
