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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.SPECIALIST_EMPTY_NAME_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ORGANIZATION_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_MAX_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MAX_ROOM_NUMBER_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MIN_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MIN_ROOM_NUMBER_LENGTH;
import static appointments.utils.Constants.SPECIALIST_WRONG_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_WRONG_ROOM_NUMBER_LENGTH;


/**
 * Класс, описывающий сущность Специалист (сотрудник, ведущий прием).
 * Содержит имя (ФИО/должность), номер кабинета,
 * флаг активности и ссылку на организацию, к которой относится специалист.
 *
 * @author yanchenko_evgeniya
 */
@Entity
@Table(name = "specialists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Specialist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    /** Должность или ФИО специалиста для отображения на сайте */
    @NotEmpty(message = SPECIALIST_EMPTY_NAME_MESSAGE)
    @Size(
            min = SPECIALIST_MIN_NAME_LENGTH,
            max = SPECIALIST_MAX_NAME_LENGTH,
            message = SPECIALIST_WRONG_NAME_LENGTH
    )
    @Column
    private String name;


    /** Номер кабинета */
    @NotEmpty(message = SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE)
    @Size(
            min = SPECIALIST_MIN_ROOM_NUMBER_LENGTH,
            max = SPECIALIST_MAX_ROOM_NUMBER_LENGTH,
            message = SPECIALIST_WRONG_ROOM_NUMBER_LENGTH
    )
    @Column(name = "room_number")
    private String roomNumber;


    /** Флаг активности специалиста */
    @Column
    private boolean active;


    /** Ссылка на организацию (объект Organization), к которой относится специалист */
    @NotNull(message = SPECIALIST_EMPTY_ORGANIZATION_MESSAGE)
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
