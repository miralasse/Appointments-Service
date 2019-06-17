package appointments.mappers;

import appointments.domain.Specialist;
import appointments.dto.SpecialistDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface SpecialistMapper {

    @Mappings({
            @Mapping(source = "organization.id", target = "organizationId")
    })
    SpecialistDTO specialistToSpecialistDto(Specialist entity);


    @Mappings({
            @Mapping(source = "organizationId", target = "organization.id")
    })
    Specialist specialistDTOToSpecialist(SpecialistDTO dto);


    List<Specialist> specialistDTOListToSpecialistList(List<SpecialistDTO> list);

    List<SpecialistDTO> specialistListToSpecialistDTOList(List<Specialist> list);

}
