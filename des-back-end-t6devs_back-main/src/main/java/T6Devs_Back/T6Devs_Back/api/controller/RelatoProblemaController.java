package T6Devs_Back.T6Devs_Back.api.controller;

import T6Devs_Back.T6Devs_Back.api.dto.RelatoProblemaDTO;
import T6Devs_Back.T6Devs_Back.api.model.service.RelatoProblemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relatos")
@RequiredArgsConstructor
public class RelatoProblemaController {

    private final RelatoProblemaService relatoService;

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<RelatoProblemaDTO> create(@RequestBody RelatoProblemaDTO relatoDTO) {
        return ResponseEntity.status(201).body(relatoService.create(relatoDTO));
    }
}