package T6Devs_Back.T6Devs_Back.api.model.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.region}") // ou @Value("${aws.cognito.region}") dependendo do que você escolher
    private String region;

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwksUri = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", region, userPoolId);
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }
}
