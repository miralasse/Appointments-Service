package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * Класс, описывающий сущность Услуга (Цель обращения).
 * Содержит название и флаг активности услуги.
 *
 * @author yanchenko_evgeniya
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Service {

    private Integer id;

    /** Поле Название услуги*/
    private String name;

    /** Поле Флаг активности */
    private boolean active;
}
