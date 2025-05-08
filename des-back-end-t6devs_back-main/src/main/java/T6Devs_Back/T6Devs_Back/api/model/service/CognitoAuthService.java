package T6Devs_Back.T6Devs_Back.api.model.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import T6Devs_Back.T6Devs_Back.api.dto.AuthResponse;
import T6Devs_Back.T6Devs_Back.api.dto.CognitoLoginRequest;
import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoAuthService {

    private final AWSCognitoIdentityProvider cognitoClient;
    private final ProfessorService professorService; // Injetar o ProfessorService

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    public AuthResponse authenticate(CognitoLoginRequest request) {
        try {
            AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(
                new AdminInitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .withClientId(clientId)
                    .withUserPoolId(userPoolId)
                    .withAuthParameters(
                        Map.of(
                            "USERNAME", request.email(),
                            "PASSWORD", request.password()
                        )
                    )
            );

            // Buscar o professor pelo email para validar o tipo
            Professor professor = professorService.getProfessorByEmail(request.email());
            String userType = professor.getTipo() != null ? professor.getTipo().toString() : "PROFESSOR";

            return new AuthResponse(
                result.getAuthenticationResult().getIdToken(),
                result.getAuthenticationResult().getRefreshToken(),
                result.getAuthenticationResult().getExpiresIn(),
                userType
            );

        } catch (NotAuthorizedException e) {
            throw new RuntimeException("Credenciais inválidas", e);
        } catch (UserNotFoundException e) {
            throw new RuntimeException("Usuário não encontrado", e);
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação: " + e.getMessage(), e);
        }
    }
}