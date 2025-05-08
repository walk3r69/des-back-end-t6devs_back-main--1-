package T6Devs_Back.T6Devs_Back.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CognitoLoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {}