package appointments.services;

import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.dto.SpecialistDTO;
import appointments.exceptions.OrganizationNotFoundException;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.mappers.SpecialistMapper;
import appointments.repos.OrganizationsRepository;
import appointments.repos.SpecialistsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static appointments.utils.Constants.ORGANIZATION_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ID_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_NOT_FOUND_MESSAGE;
import static java.util.stream.Collectors.toList;


/**
 * Класс, реализующий действия с объектами справочника Специалисты:
 * сохранение, редактирование, удаление, активация, деактивация, получение списка специалистов
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@Service
public class SpecialistsService {

    /** Поле для хранения экземпляра репозитория */
    private SpecialistsRepository specialistsRepository;

    /** Поле для хранения экземпляра репозитория организаций */
    private OrganizationsRepository organizationsRepository;

    /** Поле для хранения экземпляра маппера специалистов в DTO */
    private SpecialistMapper mapper;

    @Autowired
    public SpecialistsService(
            SpecialistsRepository specialistsRepository,
            OrganizationsRepository organizationsRepository,
            SpecialistMapper mapper
    ) {
        this.specialistsRepository = specialistsRepository;
        this.organizationsRepository = organizationsRepository;
        this.mapper = mapper;
    }


    /** Метод для добавления нового специалиста в справочник */
    @Transactional
    public SpecialistDTO addSpecialist(final SpecialistDTO dto) {

        final Specialist specialist = mapper.specialistDTOToSpecialist(dto);

        specialist.setId(null);

        specialist.setOrganization(
                organizationsRepository
                        .findById(specialist.getOrganization().getId())
                        .orElseThrow(() -> new OrganizationNotFoundException(
                                        ORGANIZATION_NOT_FOUND_MESSAGE + dto.getOrganizationId()
                                )
                        )
        );

        final Specialist savedSpecialist = specialistsRepository.save(specialist);
        log.info("Added new specialist: {}", savedSpecialist);

        return mapper.specialistToSpecialistDto(savedSpecialist);
    }

    /** Метод для поиска специалиста по идентификатору */
    @Transactional(readOnly = true)
    public SpecialistDTO findSpecialistById(final Integer id) {

        log.debug("Finding specialist with id = {}", id);

        if (id == null) {
            log.error(SPECIALIST_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SPECIALIST_EMPTY_ID_MESSAGE);
        }

        return mapper.specialistToSpecialistDto(
                specialistsRepository
                        .findById(id)
                        .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE + id))
        );
    }

    /** Метод для редактирования специалиста в справочнике */
    @Transactional
    public void editSpecialist(final SpecialistDTO dto) {

        if (dto.getId() == null) {
            log.error(SPECIALIST_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SPECIALIST_EMPTY_ID_MESSAGE);
        }

        final Specialist specialist = mapper.specialistDTOToSpecialist(dto);

        Organization organization = organizationsRepository
                .findById(specialist.getOrganization().getId())
                .orElseThrow(() -> new OrganizationNotFoundException(
                                ORGANIZATION_NOT_FOUND_MESSAGE + dto.getOrganizationId()
                        )
                );

        final Specialist foundSpecialist = specialistsRepository
                .findById(specialist.getId())
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE + specialist.getId()));

        foundSpecialist.setName(specialist.getName());
        foundSpecialist.setRoomNumber(specialist.getRoomNumber());
        foundSpecialist.setActive(specialist.isActive());
        foundSpecialist.setOrganization(organization);

        specialistsRepository.save(foundSpecialist);

        log.info("Specialist with id = {} edited: {}", specialist.getId(), foundSpecialist);
    }

    /** Метод для удаления специалиста по идентификатору */
    @Transactional
    public void removeSpecialist(final Integer id) {

        if (id == null) {
            log.error(SPECIALIST_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SPECIALIST_EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = specialistsRepository
                .findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE + id));

        specialistsRepository.delete(specialist);

        log.info("Specialist with id = {} deleted", id);
    }

    /** Метод, осуществляющий смену статуса активности специалиста */
    @Transactional
    public void changeActiveState(final Integer id, final boolean makeActive) {

        if (id == null) {
            log.error(SPECIALIST_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SPECIALIST_EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = specialistsRepository
                .findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE + id));

        specialist.setActive(makeActive);

        log.info("Specialist with id = {} {}", id, makeActive ? "activated" : "deactivated");

        specialistsRepository.save(specialist);
    }

    /** Метод для получения списка специалистов */
    @Transactional(readOnly = true)
    public List<SpecialistDTO> getSpecialists() {

        log.debug("Getting list of all specialists");

        return mapper.specialistListToSpecialistDTOList(specialistsRepository.findAll());
    }

    /** Метод для получения списка только активных специалистов */
    @Transactional(readOnly = true)
    public List<SpecialistDTO> getActiveSpecialists() {

        log.debug("Getting list of active specialists");

        return mapper.specialistListToSpecialistDTOList(
                specialistsRepository
                        .findAll()
                        .stream()
                        .filter(Specialist::isActive)
                        .collect(toList())
        );
    }
}
