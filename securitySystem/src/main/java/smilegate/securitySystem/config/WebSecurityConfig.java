package smilegate.securitySystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import smilegate.securitySystem.service.SecurityService.JwtAccessDeniedHandler;
import smilegate.securitySystem.service.SecurityService.JwtAuthenticationEntryPoint;
import smilegate.securitySystem.service.SecurityService.JwtSecurityConfig;
import smilegate.securitySystem.service.SecurityService.TokenProvider;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public WebSecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Autowired
    private DataSource dataSource;

    private static final String[] AUTH_WHITELIST = {
            "/docs/**",
            "/css/**",
            "/js/**",
            "/Image/**",
            "/member/**",
            "/h2-console/**",
            "/api/**",
            "/redirect/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

//                 enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return httpSecurity.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)
//            throws Exception {
//        httpSecurity.authorizeRequests() // HttpServletRequest 를 사용하는 요청에 대한 접근 제한을 설정한다는 의미
//                .antMatchers("/**")
//                .permitAll() // 해당 주소의 요청은 인증없이 접근을 허용하겠다는 의미
//                .anyRequest() // 나머지 요청들은 모두 인되어야한다는 의미
//                .authenticated()
//                .and()
//                .formLogin();
//
//        httpSecurity.csrf()
//                .ignoringAntMatchers("/h2-console/**","/favicon.io/**");
//        httpSecurity.headers()
//                .frameOptions()
//                .sameOrigin();
//        return httpSecurity.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}