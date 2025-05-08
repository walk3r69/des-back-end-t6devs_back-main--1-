package T6Devs_Back.T6Devs_Back.api.controller;

import T6Devs_Back.T6Devs_Back.api.dto.SolicitacaoDTO;
import T6Devs_Back.T6Devs_Back.api.model.service.SolicitacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitacaoDTO>> getAll() {
        return ResponseEntity.ok(solicitacaoService.getAll());
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<SolicitacaoDTO>> getMinhasSolicitacoes(Authentication authentication) {
        return ResponseEntity.ok(solicitacaoService.getMinhasSolicitacoes(authentication));
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<SolicitacaoDTO> create(@RequestBody SolicitacaoDTO solicitacaoDTO, Authentication authentication) {
        return ResponseEntity.status(201).body(solicitacaoService.create(solicitacaoDTO, authentication));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SolicitacaoDTO> update(@PathVariable Long id, @RequestBody SolicitacaoDTO solicitacaoDTO) {
        return ResponseEntity.ok(solicitacaoService.update(id, solicitacaoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        solicitacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}