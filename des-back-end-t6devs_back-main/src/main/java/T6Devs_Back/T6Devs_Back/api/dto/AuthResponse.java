package T6Devs_Back.T6Devs_Back.api.dto;

public record AuthResponse(
    String idToken,
    String refreshToken,
    Integer expiresIn,
    String userType
) {}