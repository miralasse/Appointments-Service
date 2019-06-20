package appointments.services;

import appointments.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Интерфейс, реализующий действия с объектами Пользователь:
 *  поиск по имени, добавление нового пользователя
 * @author yanchenko_evgeniya
 */
public interface UsersService extends UserDetailsService {

    UserDTO findByUserName(String username);
    UserDTO addUser(UserDTO userDTO);
}