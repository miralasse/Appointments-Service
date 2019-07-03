package appointments.services;

import appointments.domain.Schedule;
import appointments.dto.ScheduleDTO;
import appointments.dto.ServiceSimpleDTO;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.exceptions.ServiceNotFoundException;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.mappers.ScheduleMapper;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

import static appointments.utils.Constants.SCHEDULE_EMPTY_ID_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_INCORRECT_DATE_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SERVICE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_NOT_FOUND_MESSAGE;
import static java.util.stream.Collectors.toList;

/**
 * Класс, реализующий действия с объектами Расписание:
 * сохранение, удаление, получение списка расписаний
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@org.springframework.stereotype.Service
public class SchedulesService {

    /** Поле для хранения экземпляра репозитория */
    private SchedulesRepository schedulesRepository;

    /** Поле для хранения экземпляра репозитория услуг */
    private ServicesRepository servicesRepository;

    /** Поле для хранения экземпляра репозитория специалистов */
    private SpecialistsRepository specialistsRepository;

    /** Поле для хранения экземпляра маппера расписаний в DTO */
    private ScheduleMapper mapper;

    @Autowired
    public SchedulesService(
            SchedulesRepository schedulesRepository,
            ServicesRepository servicesRepository,
            SpecialistsRepository specialistsRepository,
            ScheduleMapper mapper
    ) {
        this.schedulesRepository = schedulesRepository;
        this.servicesRepository = servicesRepository;
        this.specialistsRepository = specialistsRepository;
        this.mapper = mapper;
    }

    /** Метод для добавления нового расписания */
    @Transactional
    public ScheduleDTO addSchedule(final ScheduleDTO dto) {

        if (dto.getDate().isBefore(LocalDate.now())) {
            log.error("Value of parameter 'date' is before current date: {}", dto.getDate());
            throw new IllegalArgumentException(SCHEDULE_INCORRECT_DATE_MESSAGE);
        }

        final Schedule schedule = mapper.scheduleDTOToSchedule(dto);

        schedule.setId(null);
        schedule.setSpecialist(
                specialistsRepository
                        .findById(dto.getSpecialist().getId())
                        .orElseThrow(() -> new SpecialistNotFoundException(
                                        SPECIALIST_NOT_FOUND_MESSAGE + dto.getSpecialist().getId()
                                )
                        )
        );
        schedule.setServices(
                dto.getServices()
                        .stream()
                        .map(ServiceSimpleDTO::getId)
                        .map(id -> servicesRepository
                                .findById(id)
                                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE + id)))
                        .collect(toList())
        );
        schedule.setReservations(new ArrayList<>());

        final Schedule savedSchedule = schedulesRepository.save(schedule);

        log.info("Added new schedule: {}", savedSchedule);

        return mapper.scheduleToScheduleDto(savedSchedule);

    }

    /** Метод для удаления расписания по идентификатору */
    @Transactional
    public void removeSchedule(final Long id) {

        if (id == null) {
            log.error(SCHEDULE_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SCHEDULE_EMPTY_ID_MESSAGE);
        }
        final Schedule schedule = schedulesRepository
                .findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE + id));

        schedulesRepository.delete(schedule);

        log.info("Schedule with id = {} deleted", id);
    }

    /** Метод для поиска расписания по идентификатору */
    @Transactional(readOnly = true)
    public ScheduleDTO findScheduleById(final Long id) {

        log.debug("Finding schedule with id = {}", id);

        if (id == null) {
            log.error(SCHEDULE_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SCHEDULE_EMPTY_ID_MESSAGE);
        }

        return mapper.scheduleToScheduleDto(
                schedulesRepository
                        .findById(id)
                        .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE + id))
        );
    }

    /** Метод для получения списка расписаний */
    @Transactional(readOnly = true)
    public Page<ScheduleDTO> getSchedules(Pageable pageable, LocalDate date) {

        log.debug("Getting list of all schedules");

        return mapper.schedulePageToScheduleDTOPage(
                schedulesRepository.findAllByDateOrderByStartTime(pageable, date)
        );
    }
}
