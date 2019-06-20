package appointments.services;

import appointments.domain.User;
import appointments.dto.UserDTO;
import appointments.exceptions.RoleNotFoundException;
import appointments.exceptions.UserAlreadyExistsException;
import appointments.mappers.UserMapper;
import appointments.repos.RolesRepository;
import appointments.repos.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static appointments.utils.Constants.ROLE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.USER_ALREADY_EXISTS_MESSAGE;
import static appointments.utils.Constants.USER_EMPTY_USERNAME_MESSAGE;
import static appointments.utils.Constants.USER_NOT_FOUND_MESSAGE;
import static java.util.Collections.singletonList;

/**
 * @author yanchenko_evgeniya
 */
@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    private static final String DEFAULT_ROLE_NAME = "ROLE_USER";


    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserMapper mapper;

    @Autowired
    public UsersServiceImpl(
            UsersRepository usersRepository,
            RolesRepository rolesRepository,
            BCryptPasswordEncoder passwordEncoder,
            UserMapper mapper
    ) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    /** Метод для поиска пользователя по уникальному имени */
    @Override
    @Transactional(readOnly = true)
    public UserDTO findByUserName(String username) {

        log.debug("Finding user with username = {}", username);

        if (username == null) {
            log.error(USER_EMPTY_USERNAME_MESSAGE);
            throw new IllegalArgumentException(USER_EMPTY_USERNAME_MESSAGE);
        }

        return mapper.userToUserDTO(
                usersRepository
                        .findOneByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE + username))
        );
    }

    /** Метод для поиска пользователя по уникальному имени (реализация метода из интерфейса UserDetailsService) */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDTO userDTO = findByUserName(username);

        return new org.springframework.security.core.userdetails.User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                new ArrayList<>(singletonList(new SimpleGrantedAuthority(userDTO.getRoleName())))
        );
    }

    /** Метод для добавления нового пользователя */
    @Override
    @Transactional
    public UserDTO addUser(UserDTO dto) {

        if (usersRepository.findOneByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE + dto.getUsername());
        }

        final User user = mapper.userDTOToUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (user.getRole().getId() == null) {
            user.setRole(
                    rolesRepository
                            .findOneByName(DEFAULT_ROLE_NAME)
                            .orElseThrow(() -> new RoleNotFoundException(ROLE_NOT_FOUND_MESSAGE + DEFAULT_ROLE_NAME))
            );
        }
        final User savedUser = usersRepository.save(user);
        log.info("Added new user: {}", savedUser);

        return mapper.userToUserDTO(savedUser);
    }


}
