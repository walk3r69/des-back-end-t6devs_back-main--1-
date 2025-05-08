package T6Devs_Back.T6Devs_Back.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import T6Devs_Back.T6Devs_Back.api.dto.LaboratoryDTO;
import T6Devs_Back.T6Devs_Back.api.model.response.ApiResponse;
import T6Devs_Back.T6Devs_Back.api.model.service.LaboratoryService;

import java.util.List;

@RestController
@RequestMapping("/api/laboratories")
@RequiredArgsConstructor
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<ApiResponse<List<LaboratoryDTO>>> getAllLaboratories() {
        List<LaboratoryDTO> laboratories = laboratoryService.getAll();
        return ResponseEntity.ok(
            ApiResponse.sucesso("Laboratórios recuperados com sucesso", laboratories)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LaboratoryDTO>> createLaboratory(
            @RequestBody @Valid LaboratoryDTO laboratoryDTO) {
        LaboratoryDTO savedLaboratory = laboratoryService.create(laboratoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso("Laboratório criado com sucesso", savedLaboratory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLaboratory(@PathVariable Long id) {
        laboratoryService.delete(id);
        return ResponseEntity.ok(
            ApiResponse.sucesso("Laboratório excluído com sucesso", null)
        );
    }
}