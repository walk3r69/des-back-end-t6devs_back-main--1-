package T6Devs_Back.T6Devs_Back.api.model.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CognitoJwtAuthFilter cognitoJwtAuthFilter;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.region}")
    private String region;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuração CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Desabilita CSRF (não necessário para APIs stateless com JWT)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Sessão stateless
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Autorização de requisições
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers(
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/public/**",
                    "/actuator/health"
                ).permitAll()
                
                // Controle granular por role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/professores/**").hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers("/api/solicitacoes/**").hasAnyRole("ADMIN", "PROFESSOR")
                
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated())
            
            // Filtro JWT Cognito
            .addFilterBefore(cognitoJwtAuthFilter, BearerTokenAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ATENÇÃO: Atualize as origens permitidas conforme o ambiente do frontend
        configuration.addAllowedOrigin("http://localhost:3000"); // Frontend React
        configuration.addAllowedOrigin("https://seu-frontend.com"); // Produção
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}