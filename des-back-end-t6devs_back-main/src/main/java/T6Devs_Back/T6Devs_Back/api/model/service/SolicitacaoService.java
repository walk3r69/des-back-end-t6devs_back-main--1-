package T6Devs_Back.T6Devs_Back.api.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import T6Devs_Back.T6Devs_Back.api.dto.SolicitacaoDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Laboratory;
import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import T6Devs_Back.T6Devs_Back.api.model.entity.Software;
import T6Devs_Back.T6Devs_Back.api.model.entity.Solicitacao;
import T6Devs_Back.T6Devs_Back.api.model.repository.LaboratoryRepository;
import T6Devs_Back.T6Devs_Back.api.model.repository.SolicitacaoRepository;
import T6Devs_Back.T6Devs_Back.api.model.repository.SoftwareRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final SoftwareRepository softwareRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final ProfessorService professorService;

    private static final List<String> VALID_STATUSES = Arrays.asList("PENDENTE", "APROVADA", "FINALIZADA");

    public List<SolicitacaoDTO> getAll() {
        return solicitacaoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitacaoDTO> getPendentes() {
        return solicitacaoRepository.findByStatus("PENDENTE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitacaoDTO> getMinhasSolicitacoes(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Professor professor = professorService.getProfessorByEmail(email);
        return solicitacaoRepository.findByProfessor(professor).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SolicitacaoDTO create(SolicitacaoDTO solicitacaoDTO, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Professor professor = professorService.getProfessorByEmail(email);

        Optional<Software> optionalSoftware = softwareRepository.findById(solicitacaoDTO.getSoftwareId());
        Optional<Laboratory> optionalLab = laboratoryRepository.findById(solicitacaoDTO.getLaboratoryId());

        if (!optionalSoftware.isPresent() || !optionalLab.isPresent()) {
            throw new RuntimeException("Software ou laboratório não encontrado");
        }

        Software software = optionalSoftware.get();
        Laboratory lab = optionalLab.get();

        if (lab.getSoftwaresInstalados().contains(software)) {
            throw new RuntimeException("Software já está instalado neste laboratório");
        }

        // Validação: dataUso deve ser futura
        if (solicitacaoDTO.getDataUso().isBefore(LocalDate.now())) {
            throw new RuntimeException("A data de uso deve ser futura");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setSoftware(software);
        solicitacao.setLaboratory(lab);
        solicitacao.setProfessor(professor);
        solicitacao.setDataUso(solicitacaoDTO.getDataUso());
        solicitacao.setHorarioUso(solicitacaoDTO.getHorarioUso());
        solicitacao.setStatus("PENDENTE");

        Solicitacao savedSolicitacao = solicitacaoRepository.save(solicitacao);
        return convertToDTO(savedSolicitacao);
    }

    public SolicitacaoDTO update(Long id, SolicitacaoDTO solicitacaoDTO) {
        Optional<Solicitacao> optionalSolicitacao = solicitacaoRepository.findById(id);
        if (optionalSolicitacao.isPresent()) {
            Solicitacao solicitacao = optionalSolicitacao.get();
            if (!solicitacao.getStatus().equals("PENDENTE")) {
                throw new RuntimeException("Apenas solicitações pendentes podem ser alteradas");
            }
            // Validação: status deve ser válido
            if (!VALID_STATUSES.contains(solicitacaoDTO.getStatus())) {
                throw new RuntimeException("Status inválido: deve ser PENDENTE, APROVADA ou FINALIZADA");
            }
            solicitacao.setStatus(solicitacaoDTO.getStatus());
            if (solicitacao.getStatus().equals("APROVADA")) {
                Laboratory lab = solicitacao.getLaboratory();
                Software software = solicitacao.getSoftware();
                lab.getSoftwaresInstalados().add(software);
                software.getLaboratorios().add(lab); // Ajustado para getLaboratorios
                laboratoryRepository.save(lab);
                softwareRepository.save(software);
            }
            Solicitacao updatedSolicitacao = solicitacaoRepository.save(solicitacao);
            return convertToDTO(updatedSolicitacao);
        }
        throw new RuntimeException("Solicitação não encontrada");
    }

    public void delete(Long id) {
        Optional<Solicitacao> optionalSolicitacao = solicitacaoRepository.findById(id);
        if (optionalSolicitacao.isPresent()) {
            Solicitacao solicitacao = optionalSolicitacao.get();
            if (!solicitacao.getStatus().equals("PENDENTE")) {
                throw new RuntimeException("Apenas solicitações pendentes podem ser excluídas");
            }
            solicitacaoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Solicitação não encontrada");
        }
    }

    private SolicitacaoDTO convertToDTO(Solicitacao solicitacao) {
        return new SolicitacaoDTO(
                solicitacao.getId(),
                solicitacao.getSoftware().getId(),
                solicitacao.getLaboratory().getId(),
                solicitacao.getProfessor().getId(),
                solicitacao.getDataUso(),
                solicitacao.getHorarioUso(),
                solicitacao.getStatus()
        );
    }
}