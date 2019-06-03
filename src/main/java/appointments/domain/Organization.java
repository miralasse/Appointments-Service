package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * Класс, описывающий сущность Организация.
 * Содержит название, фактический адрес и описание с контактной информацией.
 *
 * @author yanchenko_evgeniya
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Organization {

    private Integer id;

    /** Поле Название */
    private String name;

    /** Поле Фактический адрес */
    private String actualAddress;

    /** Поле Описание для внесения контактной и другой полезной информации */
    private String description;

}
