package appointments.mappers;

import appointments.domain.Service;
import appointments.dto.ServiceSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface ServiceSimpleMapper {

    ServiceSimpleDTO serviceToServiceSimpleDTO(Service entity);

    @Mappings({
            @Mapping(target = "active", ignore = true)
    })
    Service serviceSimpleDTOToService(ServiceSimpleDTO dto);


    List<Service> scheduleDTOListToScheduleList(List<ServiceSimpleDTO> list);

    List<ServiceSimpleDTO> scheduleListToScheduleDTOList(List<Service> list);

}
