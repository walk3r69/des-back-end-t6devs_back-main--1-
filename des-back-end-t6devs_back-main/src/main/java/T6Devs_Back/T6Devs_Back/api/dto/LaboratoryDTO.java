package T6Devs_Back.T6Devs_Back.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryDTO {
    private Long id;
    private String nome;
    private String status;
    private List<Long> softwaresInstaladosIds;
}