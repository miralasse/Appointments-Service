package appointments;

import appointments.domain.Child;
import appointments.domain.Organization;
import appointments.domain.Reservation;
import appointments.domain.Role;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.domain.User;
import appointments.integration.TestRestClient;
import appointments.repos.ChildrenRepository;
import appointments.repos.OrganizationsRepository;
import appointments.repos.ReservationsRepository;
import appointments.repos.RolesRepository;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import appointments.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

/**
 * Служебный класс, осуществляюший первичное наполнение тестовой базы
 *
 * @author yanchenko_evgeniya
 */
@Component
public class TestHelper {

    /** Поля с числовыми константами для создания тестовых объектов */

    public final static String SPECIALIST_NAME_FIRST = "Специалист 1";
    public final static String SPECIALIST_NAME_SECOND = "Специалист 2";
    public final static String SPECIALIST_NAME_THIRD = "Специалист 3";

    public final static String SERVICE_NAME_FIRST = "Постановка на очередь";
    public final static String SERVICE_NAME_SECOND = "Получение путевки в ДОО";

    private final static int BIRTH_CERTIFICATE_FIRST = 456845;
    private final static int BIRTH_CERTIFICATE_SECOND = 321856;
    private final static int BIRTH_CERTIFICATE_THIRD = 987523;

    private final static String ROOM_NUMBER_FIRST = "107";
    private final static String ROOM_NUMBER_SECOND = "109";
    private final static String ROOM_NUMBER_THIRD = "32";

    private final static String ORGANIZATION_NAME_FIRST = "Управление образования г. Белгород";
    private final static String ORGANIZATION_NAME_SECOND = "Управление образования г. Старый Оскол";
    private final static String ORGANIZATION_NAME_THIRD
            = "Департамент здравоохранения и социальной защиты населения Белгородской области";

    private final static int YEAR = 2019;
    private final static int INTERVAL = 15;
    private final static int DAY_TWELVE = 12;
    private final static int DAY_FIFTEEN = 15;

    public final static LocalDate JULY_TWELVE = LocalDate.of(YEAR, Month.JULY, DAY_TWELVE);
    public final static LocalDate JULY_FIFTEEN = LocalDate.of(YEAR, Month.JULY, DAY_FIFTEEN);
    public final static LocalDate AUGUST_TWELVE = LocalDate.of(YEAR, Month.AUGUST, DAY_TWELVE);
    public final static LocalDate AUGUST_FIFTEEN = LocalDate.of(YEAR, Month.AUGUST, DAY_FIFTEEN);

    private final static LocalTime FIRST_OFFICE_HOURS_START = LocalTime.of(8, 0);
    private final static LocalTime FIRST_OFFICE_HOURS_END = LocalTime.of(13, 0);

    private final static LocalTime SECOND_OFFICE_HOURS_START = LocalTime.of(14, 0);
    private final static LocalTime SECOND_OFFICE_HOURS_END = LocalTime.of(19, 0);

    private final static LocalTime THIRD_OFFICE_HOURS_START = LocalTime.of(10, 0);
    private final static LocalTime THIRD_OFFICE_HOURS_END = LocalTime.of(15, 0);

    public final static LocalDateTime FIRST_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.JULY, DAY_TWELVE, 9, 15);
    public final static LocalDateTime SECOND_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_TWELVE, 9, 15);
    public final static LocalDateTime THIRD_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_FIFTEEN, 11, 30);
    public final static LocalDateTime FOURTH_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_TWELVE, 11, 45);
    public final static LocalDateTime FIFTH_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_FIFTEEN, 12, 30);

    private final static String USER_ROLE = "ROLE_USER";
    private final static String ADMIN_ROLE = "ROLE_ADMIN";

    private final static String ADMIN_LOGIN = "admin";
    private final static String ADMIN_PASSWORD = "100";
    private final static String USER_LOGIN = "user";
    private final static String USER_PASSWORD = "100";

    /** Поля для хранения репозиториев */
    private ChildrenRepository childrenRepository;
    private OrganizationsRepository organizationsRepository;
    private ServicesRepository servicesRepository;
    private SpecialistsRepository specialistsRepository;
    private SchedulesRepository schedulesRepository;
    private ReservationsRepository reservationsRepository;
    private RolesRepository rolesRepository;
    private UsersRepository usersRepository;

    @Autowired
    public TestHelper(
            ChildrenRepository childrenRepository,
            OrganizationsRepository organizationsRepository,
            ServicesRepository servicesRepository,
            SpecialistsRepository specialistsRepository,
            SchedulesRepository schedulesRepository,
            ReservationsRepository reservationsRepository,
            RolesRepository rolesRepository,
            UsersRepository usersRepository
    ) {
        this.childrenRepository = childrenRepository;
        this.organizationsRepository = organizationsRepository;
        this.servicesRepository = servicesRepository;
        this.specialistsRepository = specialistsRepository;
        this.schedulesRepository = schedulesRepository;
        this.reservationsRepository = reservationsRepository;
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
    }

    /** Метод для логина пользователя с ролью ADMIN и получения идентификатора сессии */
    public String loginAsAdmin(TestRestClient restClient) {
        return restClient.login(ADMIN_LOGIN, ADMIN_PASSWORD);
    }

    /** Метод для логина пользователя с ролью USER и получения идентификатора сессии */
    public String loginAsUser(TestRestClient restClient) {
        return restClient.login(USER_LOGIN, USER_PASSWORD);
    }

    /** Метод для очистки и первичного наполнения всех таблиц */
    public void refill() {
        clearAll();
        initAll();
    }


    /** Метод для первичного наполнения всех таблиц */
    public void initAll() {
        initRoles();
        initUsers();
        initChildren();
        initOrganizations();
        initServices();
        initSpecialists();
        initSchedules();
        initReservations();
    }


    public void initRoles() {
        rolesRepository.save(new Role(null, USER_ROLE));
        rolesRepository.save(new Role(null, ADMIN_ROLE));
    }

    public void initUsers() {

        usersRepository.save(new User(
                        null,
                        "user",
                        "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i",
                        "Mary",
                        "Green",
                        "user@gmail.com",
                        "+79203333333",
                        rolesRepository.findOneByName(USER_ROLE).get()
                )
        );

        usersRepository.save(new User(
                null,
                "admin",
                "$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i",
                "John",
                "Smith",
                "admin@gmail.com",
                "+79201111111",
                rolesRepository.findOneByName(ADMIN_ROLE).get()
                )
        );
    }

    /** Метод для первичного наполнения таблицы children */
    public void initChildren() {

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

    /** Метод для первичного наполнения таблицы organization */
    public void initOrganizations() {

        organizationsRepository.save(new Organization(
                null, ORGANIZATION_NAME_FIRST,
                "г. Белгород, ул. Попова 25а", "+7(4722)32-68-95"
        ));

        organizationsRepository.save(new Organization(
                null, ORGANIZATION_NAME_SECOND,
                "Белгородская обл., г. Старый Оскол, ул. Комсомольская, 43", "+7(4725)22‑03-38"
        ));

        organizationsRepository.save(new Organization(
                null, ORGANIZATION_NAME_THIRD,
                "г. Белгород, Свято-Троицкий бул., 18", "+7(4722)23‑56-46"
        ));
    }

    /** Метод для первичного наполнения таблицы services */
    public void initServices() {

        servicesRepository.save(new Service(
                null, SERVICE_NAME_FIRST, true
        ));

        servicesRepository.save(new Service(
                null, SERVICE_NAME_SECOND, true
        ));
    }

    /** Метод для первичного наполнения таблицы specialists */
    public void initSpecialists() {

        specialistsRepository.save(new Specialist(
                null, SPECIALIST_NAME_FIRST, ROOM_NUMBER_FIRST, true,
                organizationsRepository.findOneByName(ORGANIZATION_NAME_FIRST).orElse(null)
        ));

        specialistsRepository.save(new Specialist(
                null, SPECIALIST_NAME_SECOND, ROOM_NUMBER_SECOND, true,
                organizationsRepository.findOneByName(ORGANIZATION_NAME_FIRST).orElse(null)
        ));

        specialistsRepository.save(new Specialist(
                null, SPECIALIST_NAME_THIRD, ROOM_NUMBER_THIRD, true,
                organizationsRepository.findOneByName(ORGANIZATION_NAME_SECOND).orElse(null)
        ));
    }

    /** Метод для первичного наполнения таблицы schedules */
    public void initSchedules() {

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_FIRST).orElse(null),
                JULY_TWELVE,
                servicesRepository.findAll(),
                FIRST_OFFICE_HOURS_START,
                FIRST_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>(),
                true
        ));

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_SECOND).orElse(null),
                JULY_FIFTEEN,
                servicesRepository.findAll(),
                THIRD_OFFICE_HOURS_START,
                THIRD_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>(),
                true
        ));

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_FIRST).orElse(null),
                AUGUST_TWELVE,
                servicesRepository.findAll(),
                FIRST_OFFICE_HOURS_START,
                FIRST_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>(),
                true
        ));

        schedulesRepository.save(new Schedule(
                null,
                specialistsRepository.findOneByName(SPECIALIST_NAME_SECOND).orElse(null),
                AUGUST_FIFTEEN,
                servicesRepository.findAll(),
                SECOND_OFFICE_HOURS_START,
                SECOND_OFFICE_HOURS_END,
                INTERVAL,
                new ArrayList<>(),
                true
        ));
    }

    /** Метод для первичного наполнения таблицы reservations */
    public void initReservations() {

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
    public void clearAll() {
        clearReservations();
        clearSchedules();
        clearSpecialists();
        clearServices();
        clearOrganizations();
        clearChildren();
        clearUsers();
        clearRoles();
    }

    /** Метод для очистки таблицы children */
    public void clearChildren() {
        childrenRepository.deleteAll();
    }

    /** Метод для очистки таблицы organizations */
    public void clearOrganizations() {
        organizationsRepository.deleteAll();
    }

    /** Метод для очистки таблицы services */
    public void clearServices() {
        servicesRepository.deleteAll();
    }

    /** Метод для очистки таблицы specialists */
    public void clearSpecialists() {
        specialistsRepository.deleteAll();
    }

    /** Метод для очистки таблицы schedules */
    public void clearSchedules() {
        schedulesRepository.deleteAll();
    }

    /** Метод для очистки таблицы reservations */
    public void clearReservations() {
        reservationsRepository.deleteAll();
    }

    /** Метод для очистки таблицы users */
    public void clearUsers() {
        usersRepository.deleteAll();
    }

    /** Метод для очистки таблицы roles */
    public void clearRoles() {
        rolesRepository.deleteAll();
    }

}
