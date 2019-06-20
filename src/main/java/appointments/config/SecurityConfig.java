package appointments.config;

import appointments.services.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static appointments.utils.Constants.SECURITY_ROLE_ADMIN_NAME;
import static appointments.utils.Constants.SECURITY_ROLE_USER_NAME;

/**
 * @author yanchenko_evgeniya
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsersService usersService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    public SecurityConfig(
            UsersService usersService,
            BCryptPasswordEncoder passwordEncoder,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeRequests()
                    .antMatchers("/welcome", "/registration", "/login*")
                        .permitAll()
                    .antMatchers("/")
                        .authenticated()
                    .antMatchers(
                            HttpMethod.GET, "/services/active", "/specialists/active", "/schedules/active"
                    )
                        .permitAll()
                    .antMatchers(HttpMethod.POST, "/reservations/**")
                        .hasAnyRole(SECURITY_ROLE_USER_NAME, SECURITY_ROLE_ADMIN_NAME)
                    .antMatchers(HttpMethod.GET, "/reservations/**")
                        .hasAnyRole(SECURITY_ROLE_USER_NAME, SECURITY_ROLE_ADMIN_NAME)
                    .antMatchers(HttpMethod.POST, "/services/**", "/specialists/**", "/schedules/**")
                        .hasRole(SECURITY_ROLE_ADMIN_NAME)
                    .antMatchers(
                            HttpMethod.GET, "/services/**", "/specialists/**", "/schedules/**"
                    )
                        .hasRole(SECURITY_ROLE_ADMIN_NAME)
                    .antMatchers(HttpMethod.DELETE, "/services/**", "/specialists/**", "/schedules/**")
                        .hasRole(SECURITY_ROLE_ADMIN_NAME)
                    .antMatchers(HttpMethod.PATCH, "/specialists/**", "/services/**")
                        .hasRole(SECURITY_ROLE_ADMIN_NAME)
                    .antMatchers(HttpMethod.PUT, "/specialists/**")
                        .hasRole(SECURITY_ROLE_ADMIN_NAME)
                .and()
                    .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                .and()
                    .logout()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                .and()
                    .csrf().disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(usersService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

}


