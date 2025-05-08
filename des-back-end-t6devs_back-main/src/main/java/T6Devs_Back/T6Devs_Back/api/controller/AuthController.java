package T6Devs_Back.T6Devs_Back.api.controller;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AWSCognitoIdentityProvider cognitoClient;

    @Autowired
    public AuthController(AWSCognitoIdentityProvider cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", username);
        authParams.put("PASSWORD", password);

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .withClientId("5f5flgtvn74fu9hqjdg1jhdvcp") // Client ID do Cognito (do awsConfig.js)
                .withUserPoolId("sa-east-1_beCUh1uj") // User Pool ID (do awsConfig.js)
                .withAuthParameters(authParams);

        try {
            AdminInitiateAuthResult authResult = cognitoClient.adminInitiateAuth(authRequest);
            Map<String, String> response = new HashMap<>();
            response.put("idToken", authResult.getAuthenticationResult().getIdToken());
            response.put("accessToken", authResult.getAuthenticationResult().getAccessToken());
            response.put("refreshToken", authResult.getAuthenticationResult().getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar: " + e.getMessage());
        }
    }
}