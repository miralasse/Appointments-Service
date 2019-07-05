package appointments.config;

import appointments.domain.Child;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.repos.ChildrenRepository;
import appointments.repos.OrganizationsRepository;
import appointments.repos.ReservationsRepository;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

/** Класс для первичного наполнения базы данных
 *
 * @author yanchenko_evgeniya
 */
@SuppressWarnings("Duplicates")
@Component
public class DataLoader implements ApplicationRunner {

    /** Поля с числовыми константами для создания тестовых объектов */

    private final static String SPECIALIST_NAME_FIRST = "Специалист 1";
    private final static String SPECIALIST_NAME_SECOND = "Специалист 2";
    private final static String SPECIALIST_NAME_THIRD = "Специалист 3";

    private final static String SERVICE_NAME_FIRST = "Постановка на очередь";
    private final static String SERVICE_NAME_SECOND = "Получение путевки в ДОО";

    private final static int BIRTH_CERTIFICATE_FIRST = 456845;
    private final static int BIRTH_CERTIFICATE_SECOND = 321856;
    private final static int BIRTH_CERTIFICATE_THIRD = 987523;

    private final static String ROOM_NUMBER_FIRST = "107";
    private final static String ROOM_NUMBER_SECOND = "109";
    private final static String ROOM_NUMBER_THIRD = "32";

    private final static String DEFAULT_ORGANIZATION_NAME = "Отделение ПФР по Белгородской области";

    private final static int YEAR = 2019;
    private final static int INTERVAL = 15;
    private final static int DAY_TWELVE = 12;
    private final static int DAY_FIFTEEN = 15;

    private final static LocalDate JULY_TWELVE = LocalDate.of(YEAR, Month.JULY, DAY_TWELVE);
    private final static LocalDate JULY_FIFTEEN = LocalDate.of(YEAR, Month.JULY, DAY_FIFTEEN);
    private final static LocalDate AUGUST_TWELVE = LocalDate.of(YEAR, Month.AUGUST, DAY_TWELVE);
    private final static LocalDate AUGUST_FIFTEEN = LocalDate.of(YEAR, Month.AUGUST, DAY_FIFTEEN);

    private final static LocalTime FIRST_OFFICE_HOURS_START = LocalTime.of(8, 0);
    private final static LocalTime FIRST_OFFICE_HOURS_END = LocalTime.of(13, 0);

    private final static LocalTime SECOND_OFFICE_HOURS_START = LocalTime.of(14, 0);
    private final static LocalTime SECOND_OFFICE_HOURS_END = LocalTime.of(19, 0);

    private final static LocalTime THIRD_OFFICE_HOURS_START = LocalTime.of(10, 0);
    private final static LocalTime THIRD_OFFICE_HOURS_END = LocalTime.of(15, 0);

    private final static LocalDateTime FIRST_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.JULY, DAY_TWELVE, 9, 15);
    private final static LocalDateTime SECOND_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_TWELVE, 9, 15);
    private final static LocalDateTime THIRD_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_FIFTEEN, 11, 30);
    private final static LocalDateTime FOURTH_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_TWELVE, 11, 45);
    private final static LocalDateTime FIFTH_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_FIFTEEN, 12, 30);


    /** Поля для хранения репозиториев */
    private ChildrenRepository childrenRepository;
    private OrganizationsRepository organizationsRepository;
    private ServicesRepository servicesRepository;
    private SpecialistsRepository specialistsRepository;
    private SchedulesRepository schedulesRepository;
    private ReservationsRepository reservationsRepository;

    @Autowired
    public DataLoader(
            ChildrenRepository childrenRepository,
            OrganizationsRepository organizationsRepository,
            ServicesRepository servicesRepository,
            SpecialistsRepository specialistsRepository,
            SchedulesRepository schedulesRepository,
            ReservationsRepository reservationsRepository
    ) {
        this.childrenRepository = childrenRepository;
        this.organizationsRepository = organizationsRepository;
        this.servicesRepository = servicesRepository;
        this.specialistsRepository = specialistsRepository;
        this.schedulesRepository = schedulesRepository;
        this.reservationsRepository = reservationsRepository;

    }

    /** Метод для очистки и первичного наполнения всех таблиц */
    private void refill() {
        clearAll();
        initAll();
    }


    /** Метод для первичного наполнения всех таблиц */
    private void initAll() {
        initChildren();
        initServices();
        initSpecialists();
        initSchedules();
        initReservations();
    }


    /** Метод для первичного наполнения таблицы children */
    private void initChildren() {

        childrenRepository.save(new Child(
                null, "I-56", BIRTH_CERTIFICATE_FIRST,
                "+7(910)452-85-96", "example1@mail.ru",
                "Иванов", "Иван", "Иванович"
        ));

        childrenRepository.save(new Child(
                null, "I-57", BIRTH_CERTIFICATE_SECOND,
                "+7(910)785-12-36", "example2@mail.ru",
                "Кузнецова", "Елена", "Александровна"
        ));

        childrenRepository.save(new Child(
                null, "I-58",
                BIRTH_CERTIFICATE_THIRD, "+7(920)623-85-47", "example3@mail.ru",
                "Сергеев", "Антон", "Олегович"
        ));
    }

    /** Метод для первичного наполнения таблицы services */
    private void initServices() {

        servicesRepository.save(new Service(
                null, SERVICE_NAME_FIRST, true
        ));

        servicesRepository.save(new Service(
                null, SERVICE_NAME_SECOND, true
        ));
    }

    /** Метод для первичного наполнения таблицы specialists */
    private void initSpecialists() {

        specialistsRepository.save(new Specialist(
                null, SPECIALIST_NAME_FIRST, true,
                organizationsRepository.findOneByName(DEFAULT_ORGANIZATION_NAME).orElse(null)
        ));

        specialistsRepository.save(new Specialist(
                null, SPECIALIST_NAME_SECOND, true,
                organizationsRepository.findOneByName(DEFAULT_ORGANIZATION_NAME).orElse(null)
        ));

        specialistsRepository.save(new Specialist(
                null, SPECIALIST_NAME_THIRD, true,
                organizationsRepository.findOneByName(DEFAULT_ORGANIZATION_NAME).orElse(null)
        ));
    }

    /** Метод для первичного наполнения таблицы schedules */
    private void initSchedules() {

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_FIRST).orElse(null),
                ROOM_NUMBER_FIRST,
                JULY_TWELVE,
                servicesRepository.findAll(),
                FIRST_OFFICE_HOURS_START,
                FIRST_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>()
        ));

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_SECOND).orElse(null),
                ROOM_NUMBER_SECOND,
                JULY_FIFTEEN,
                servicesRepository.findAll(),
                THIRD_OFFICE_HOURS_START,
                THIRD_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>()
        ));

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_FIRST).orElse(null),
                ROOM_NUMBER_THIRD,
                AUGUST_TWELVE,
                servicesRepository.findAll(),
                FIRST_OFFICE_HOURS_START,
                FIRST_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>()
        ));

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_SECOND).orElse(null),
                ROOM_NUMBER_FIRST,
                AUGUST_FIFTEEN,
                servicesRepository.findAll(),
                SECOND_OFFICE_HOURS_START,
                SECOND_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>()
        ));
    }

    /** Метод для первичного наполнения таблицы reservations */
    private void initReservations() {

        initReservationsForJulyTwelve();
        initReservationsForAugustTwelve();
        initReservationsForAugustFifteen();
    }

    /** Служебный метод для добавления тестовых записей на прием в конкретную дату 2019-07-12 */
    private void initReservationsForJulyTwelve() {

        final Schedule scheduleSpecialistOneJulyTwelve = schedulesRepository
                .findOneBySpecialistAndDate(
                        specialistsRepository
                                .findOneByName(SPECIALIST_NAME_FIRST)
                                .orElse(null),
                        JULY_TWELVE
                )
                .orElse(null);

        final Reservation firstReservation = reservationsRepository.save(new Reservation(
                null,
                FIRST_RESERVATION_HOUR,
                scheduleSpecialistOneJulyTwelve,
                servicesRepository.findOneByName(SERVICE_NAME_FIRST).orElse(null),
                true,
                childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE_FIRST).orElse(null)
        ));

        scheduleSpecialistOneJulyTwelve.getReservations().add(firstReservation);
    }

    /** Служебный метод для добавления тестовых записей на прием в конкретную дату 2019-08-12 */
    private void initReservationsForAugustTwelve() {

        final Schedule scheduleSpecialistOneAugustTwelve = schedulesRepository
                .findOneBySpecialistAndDate(
                        specialistsRepository
                                .findOneByName(SPECIALIST_NAME_FIRST)
                                .orElse(null),
                        AUGUST_TWELVE
                )
                .orElse(null);

        final Reservation firstReservation = reservationsRepository.save(new Reservation(
                null,
                SECOND_RESERVATION_HOUR,
                scheduleSpecialistOneAugustTwelve,
                servicesRepository.findOneByName(SERVICE_NAME_FIRST).orElse(null),
                true,
                childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE_SECOND).orElse(null)
        ));

        final Reservation secondReservation = reservationsRepository.save(new Reservation(
                null,
                FOURTH_RESERVATION_HOUR,
                scheduleSpecialistOneAugustTwelve,
                servicesRepository.findOneByName(SERVICE_NAME_FIRST).orElse(null),
                true,
                childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE_FIRST).orElse(null)
        ));

        scheduleSpecialistOneAugustTwelve.getReservations().add(firstReservation);
        scheduleSpecialistOneAugustTwelve.getReservations().add(secondReservation);
    }

    /** Служебный метод для добавления тестовых записей на прием в конкретную дату 2019-08-15 */
    private void initReservationsForAugustFifteen() {

        final Schedule scheduleSpecialistTwoAugustFifteen = schedulesRepository
                .findOneBySpecialistAndDate(
                        specialistsRepository
                                .findOneByName(SPECIALIST_NAME_SECOND)
                                .orElse(null),
                        AUGUST_FIFTEEN
                )
                .orElse(null);

        final Reservation firstReservation = reservationsRepository.save(new Reservation(
                null,
                THIRD_RESERVATION_HOUR,
                scheduleSpecialistTwoAugustFifteen,
                servicesRepository.findOneByName(SERVICE_NAME_SECOND).orElse(null),
                true,
                childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE_SECOND).orElse(null)
        ));

        final Reservation secondReservation = reservationsRepository.save(new Reservation(
                null,
                FIFTH_RESERVATION_HOUR,
                scheduleSpecialistTwoAugustFifteen,
                servicesRepository.findOneByName(SERVICE_NAME_SECOND).orElse(null),
                true,
                childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE_THIRD).orElse(null)
        ));

        scheduleSpecialistTwoAugustFifteen.getReservations().add(firstReservation);
        scheduleSpecialistTwoAugustFifteen.getReservations().add(secondReservation);
    }



    /** Метод для очистки всех таблиц */
    private void clearAll() {
        clearReservations();
        clearSchedules();
        clearSpecialists();
        clearServices();
        clearChildren();
    }

    /** Метод для очистки таблицы children */
    private void clearChildren() {
        childrenRepository.deleteAll();
    }

    /** Метод для очистки таблицы services */
    private void clearServices() {
        servicesRepository.deleteAll();
    }

    /** Метод для очистки таблицы specialists */
    private void clearSpecialists() {
        specialistsRepository.deleteAll();
    }

    /** Метод для очистки таблицы schedules */
    private void clearSchedules() {
        schedulesRepository.deleteAll();
    }

    /** Метод для очистки таблицы reservations */
    private void clearReservations() {
        reservationsRepository.deleteAll();
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        refill();
    }
}