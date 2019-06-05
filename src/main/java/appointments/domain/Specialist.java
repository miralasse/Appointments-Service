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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    private static final int MIN_NAME_LENGTH = 8;
    private static final int MAX_NAME_LENGTH = 400;

    private static final int MIN_ROOM_NUMBER_LENGTH = 1;
    private static final int MAX_ROOM_NUMBER_LENGTH = 16;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    /** Поле для указания должности или ФИО специалиста для отображения на сайте */
    @NotNull(message = "Должность/ФИО специалиста должно быть указано")
    @Size(
            min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Длина должности/ФИО специалиста должна быть от "
                    + MIN_NAME_LENGTH
                    + " до "
                    + MAX_NAME_LENGTH
                    + " символов"
    )
    @Column
    private String name;


    /** Поле Номер кабинета */
    @NotNull(message = "Номер кабинета специалиста должен быть указан")
    @Size(
            min = MIN_ROOM_NUMBER_LENGTH,
            max = MAX_ROOM_NUMBER_LENGTH,
            message = "Длина номера кабинета должна быть от "
                    + MIN_ROOM_NUMBER_LENGTH
                    + " до "
                    + MAX_ROOM_NUMBER_LENGTH
                    + " символов"
    )
    @Column(name = "room_number")
    private String roomNumber;


    /** Поле Флаг активности специалиста */
    @Column
    private boolean active;


    /** Поле Ссылка на организацию (объект Organization), к которой относится специалист */
    @NotNull(message = "Организация специалиста должна быть указана")
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
