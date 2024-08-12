package app.config;

import app.component.JwtComponent;
import app.reprository.UserRepository;
import app.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import app.service.UserService;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    public UserService userService;
    private JwtComponent jwtComponent;
    private UserRepository userRepository;

    private JwtService jwtService;


    @Autowired
    public WebSecurityConfig(UserService userService,
                             JwtComponent jwtComponent,
                             UserRepository userRepository
                             ) {

        this.userService = userService;
        this.jwtComponent = jwtComponent;
        this.userRepository = userRepository;



    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests.requestMatchers("/registration", "/auth", "/exit","api/auth/refreshtoken",
                        "api/auth/login","api/auth/accesstoken").permitAll()
                        .anyRequest().authenticated()
        ).csrf((arg) -> arg.disable())
                .addFilter(new AuthenticationFilter(authenticationManager(),jwtComponent, userRepository));
                //.httpBasic(Customizer.withDefaults());




        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {

        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}


