package appointments.mappers;

import appointments.domain.Specialist;
import appointments.dto.SpecialistSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface SpecialistSimpleMapper {

    SpecialistSimpleDTO specialistToSpecialistSimpleDTO(Specialist entity);

    @Mappings({
            @Mapping(target = "active", ignore = true),
            @Mapping(target = "organization", ignore = true)
    })
    Specialist specialistSimpleDTOToSpecialist(SpecialistSimpleDTO dto);

}
