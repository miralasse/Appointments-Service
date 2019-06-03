package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, описывающий сущность Ребёнок.
 * Содержит серию и номер свидельства о рождении, ФИО и контактную информацию
 *
 * @author yanchenko_evgeniya
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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

}
