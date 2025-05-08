package T6Devs_Back.T6Devs_Back.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    String code,
    String message,
    LocalDateTime timestamp,
    List<String> details //erros de validação
) {}