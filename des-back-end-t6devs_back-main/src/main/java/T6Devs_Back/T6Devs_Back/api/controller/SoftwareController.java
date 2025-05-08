package T6Devs_Back.T6Devs_Back.api.controller;

import T6Devs_Back.T6Devs_Back.api.dto.SoftwareDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Laboratory;
import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import T6Devs_Back.T6Devs_Back.api.model.mapper.SoftwareMapper;
import T6Devs_Back.T6Devs_Back.api.model.repository.LaboratoryRepository;
import T6Devs_Back.T6Devs_Back.api.model.service.ProfessorService;
import T6Devs_Back.T6Devs_Back.api.model.service.SoftwareService;
import T6Devs_Back.T6Devs_Back.api.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/softwares")
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareService softwareService;
    private final LaboratoryRepository laboratoryRepository;
    private final ProfessorService professorService;
    private final SoftwareMapper softwareMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<List<SoftwareDTO>> getAll() {
        return ResponseEntity.ok(softwareService.getAll());
    }

    @GetMapping("/disponiveis")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<List<SoftwareDTO>> getAllDisponiveis() {
        return ResponseEntity.ok(softwareService.getAllDisponiveis());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoftwareDTO> create(@RequestBody SoftwareDTO softwareDTO) {
        return ResponseEntity.status(201).body(softwareService.create(softwareDTO));
    }

    @PutMapping("/{id}/toggle-availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoftwareDTO> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(softwareService.toggleAvailability(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        softwareService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{softwareId}/laboratorios/{labId}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> removeSoftwareFromLab(@PathVariable Long softwareId, @PathVariable Long labId) {
        softwareService.removeSoftwareFromLab(softwareId, labId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirmar-uso")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> confirmSoftwareUsage(@RequestBody Map<String, Long> request, Authentication authentication) {
        Long softwareId = request.get("softwareId");
        softwareService.confirmUsage(softwareId, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/laboratorios/{labId}/softwares")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<SoftwareDTO>> getSoftwaresByLab(@PathVariable Long labId, Authentication authentication) {
        // Buscar o laboratório
        Laboratory lab = laboratoryRepository.findById(labId)
                .orElseThrow(() -> new NotFoundException("Laboratório não encontrado"));

        // Obter o professor autenticado
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Professor professor = professorService.getProfessorByEmail(email);

        // Validar se o professor tem o tipo correto
        List<String> groups = (List<String>) jwt.getClaim("cognito:groups");
        String cognitoRole = groups != null && groups.contains("ADMIN") ? "ADMIN" : "PROFESSOR";
        if (professor == null) {
            throw new NotFoundException("Professor não encontrado");
        }
        if (!professor.getTipo().toString().equals(cognitoRole)) {
            throw new IllegalStateException("O tipo do professor no banco de dados não corresponde ao grupo do Cognito");
        }

        // Verificar se o professor tem acesso ao laboratório
        // Aqui talvez adicionaremos uma lógica mais robusta de como verificar se o professor está associado ao laboratório
        if (professor == null) {
            throw new NotFoundException("Professor não encontrado");
        }

        // Retornar a lista de softwares instalados no laboratório
        List<SoftwareDTO> softwareDTOs = lab.getSoftwaresInstalados().stream()
                .map(softwareMapper::toDto)
                .toList();
        return ResponseEntity.ok(softwareDTOs);
    }
}