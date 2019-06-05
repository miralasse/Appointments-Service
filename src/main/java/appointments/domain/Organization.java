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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Класс, описывающий сущность Организация.
 * Содержит название, фактический адрес и описание с контактной информацией.
 *
 * @author yanchenko_evgeniya
 */
@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Organization {

    private static final int MIN_STRING_LENGTH = 12;
    private static final int MAX_STRING_LENGTH = 400;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    /** Поле Название */
    @NotNull(message = "Наименование организации должно быть указано")
    @Size(
            min = MIN_STRING_LENGTH,
            max = MAX_STRING_LENGTH,
            message = "Длина наименования организации должна быть от "
                    + MIN_STRING_LENGTH
                    + " до "
                    + MAX_STRING_LENGTH
                    + " символов"
    )
    @Column
    private String name;


    /** Поле Фактический адрес */
    @NotNull(message = "Фактический адрес организации должен быть указан")
    @Size(
            min = MIN_STRING_LENGTH,
            max = MAX_STRING_LENGTH,
            message = "Длина адреса организации должна быть от "
                    + MIN_STRING_LENGTH
                    + " до "
                    + MAX_STRING_LENGTH
                    + " символов"
    )
    @Column(name = "actual_address")
    private String actualAddress;


    /** Поле Описание для внесения контактной и другой полезной информации */
    @NotNull(message = "Контактная информация организации должна быть указана")
    @Size(
            min = MIN_STRING_LENGTH,
            max = MAX_STRING_LENGTH,
            message = "Длина контактной информации должна быть от "
                    + MIN_STRING_LENGTH
                    + " до "
                    + MAX_STRING_LENGTH
                    + " символов"
    )
    @Column
    private String description;

}
