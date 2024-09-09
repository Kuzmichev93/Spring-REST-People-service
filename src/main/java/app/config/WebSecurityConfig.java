package app.config;

import app.component.JwtComponent;
import app.reprository.UserRepository;
import app.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import app.service.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    public UserService userService;
    private JwtComponent jwtComponent;
    private UserRepository userRepository;

    private JwtService jwtService;


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
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
                authorizeHttpRequests.requestMatchers("/registration",
                        "api/users",
                        "im/{name}/{test}"
                        ,"tv/create",
                        "tv/product/{id}","tv/products","tv/pagination","tv/ids",
                        "laptop/create","laptop/product/{id}","laptop/products","laptop/pagination","laptop/ids",
                        "/auth", "/exit","api/auth/refreshtoken",
                        "api/auth/login","api/auth/accesstoken").permitAll()
                        .anyRequest().authenticated()
        ).csrf((arg) -> arg.disable())
                .addFilter(new AuthenticationFilter(authenticationManager(),jwtComponent, userRepository,resolver))
                //.cors((arg)->arg.configurationSource((request -> customCorsConfiguration())));
                .cors((arg)->arg.configurationSource(request -> customCorsConfiguration()));
                //.httpBasic(Customizer.withDefaults());

        return http.build();

    }


    public CorsConfiguration customCorsConfiguration(){
        CorsConfiguration corsConfiguration = new CustomCorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET","POST","OPTIONS","DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Size","Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));


        return corsConfiguration.applyPermitDefaultValues();
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


