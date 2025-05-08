package T6Devs_Back.T6Devs_Back.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Modelo padrão de resposta da API")
public class ApiResponse<T> {
    
    @Schema(description = "Indica se a requisição foi bem-sucedida", example = "true")
    private final boolean sucesso;
    
    @Schema(description = "Mensagem descritiva da resposta", example = "Operação realizada com sucesso")
    private final String mensagem;
    
    @Schema(description = "Dados retornados pela requisição")
    private final T dados;

    // Construtor privado para forçar uso dos métodos fábrica
    private ApiResponse(boolean sucesso, String mensagem, T dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    // Métodos fábrica
    public static <T> ApiResponse<T> criar(boolean sucesso, String mensagem, T dados) {
        return new ApiResponse<>(sucesso, mensagem, dados);
    }

    public static <T> ApiResponse<T> sucesso(T dados) {
        return new ApiResponse<>(true, "Operação realizada com sucesso", dados);
    }

    public static <T> ApiResponse<T> sucesso(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados);
    }

    public static <T> ApiResponse<T> erro(String mensagem) {
        return new ApiResponse<>(false, mensagem, null);
    }

    public static ApiResponse<Void> sucesso(String mensagem) {
        return new ApiResponse<>(true, mensagem, null);
    }
}