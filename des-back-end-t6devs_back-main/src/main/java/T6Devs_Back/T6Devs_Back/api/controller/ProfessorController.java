package T6Devs_Back.T6Devs_Back.api.controller;

import T6Devs_Back.T6Devs_Back.api.dto.ProfessorDTO;
import T6Devs_Back.T6Devs_Back.api.model.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfessorDTO>> getAll() {
        return ResponseEntity.ok(professorService.getAll()); // Corrigido de findAll() para getAll()
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfessorDTO> create(@RequestBody ProfessorDTO professorDTO) {
        return ResponseEntity.status(201).body(professorService.create(professorDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}