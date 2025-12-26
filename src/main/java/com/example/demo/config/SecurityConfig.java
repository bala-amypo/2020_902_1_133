@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/login").permitAll()
                .anyRequest().authenticated()
            )

            .userDetailsService(userDetailsService)

            .formLogin(form -> form
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout.permitAll());

        http.headers(headers ->
            headers.frameOptions(frame -> frame.disable())
        );

        return http.build();
    }
}
