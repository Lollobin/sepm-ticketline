package at.ac.tuwien.sepm.groupphase.backend.config;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtAuthenticationFilter;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtAuthorizationFilter;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.security.LoginFailureHandler;
import at.ac.tuwien.sepm.groupphase.backend.security.LoginSuccessHandler;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    private final JwtTokenizer jwtTokenizer;
    private final LoginFailureHandler failureCustomHandler;
    private final LoginSuccessHandler successCustomHandler;

    @Autowired
    public SecurityConfig(
        UserService userService,
        PasswordEncoder passwordEncoder,
        SecurityProperties securityProperties,
        JwtTokenizer jwtTokenizer,
        LoginFailureHandler failureCustomHandler,
        LoginSuccessHandler successCustomHandler) {
        this.userService = userService;
        this.securityProperties = securityProperties;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.failureCustomHandler = failureCustomHandler;
        this.successCustomHandler = successCustomHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf()
            .disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .authorizeRequests()
            //h2-console enable..
            .antMatchers("/h2-console/**").permitAll()

            //please add your route here if it should be included in the "Public View" for nonauthenticated users.
            .antMatchers(HttpMethod.GET, "/events/**").permitAll()
            .antMatchers(HttpMethod.GET, "/shows/**").permitAll()
            .antMatchers(HttpMethod.GET, "/artists/**").permitAll()
            .antMatchers(HttpMethod.GET, "/locations/**").permitAll()
            .antMatchers(HttpMethod.GET, "/seatingPlan/**").permitAll()
            .antMatchers(HttpMethod.GET, "/topShows").permitAll()
            .antMatchers(HttpMethod.GET, "/seatingPlanLayouts").permitAll()
            .antMatchers(HttpMethod.GET, "/topEvents").permitAll()
            .antMatchers(HttpMethod.GET, "/articles").permitAll()
            .antMatchers(HttpMethod.GET, "/articles/**").permitAll()
            .antMatchers(HttpMethod.GET, "/images/**").permitAll()

            //for login and registration
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .antMatchers(HttpMethod.POST, "/users").permitAll()
            .antMatchers(HttpMethod.POST, "/passwordReset").permitAll()
            .antMatchers(HttpMethod.POST, "/passwordUpdate").permitAll()

            .anyRequest().authenticated();

        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(authenticationManager(),
            securityProperties, jwtTokenizer, failureCustomHandler, successCustomHandler);

        http.addFilter(customFilter)
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), securityProperties))

        ;

        // enable h2-console (also check for antmatchers above for h2-console when removing)
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final List<String> permitAll = Collections.singletonList("*");
        final List<String> permitMethods =
            List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.TRACE.name());
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(permitAll);
        configuration.setAllowedOrigins(permitAll);
        configuration.setAllowedMethods(permitMethods);
        configuration.addExposedHeader("Location");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
