package T6Devs_Back.T6Devs_Back.api.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import T6Devs_Back.T6Devs_Back.api.dto.SoftwareDTO;
import T6Devs_Back.T6Devs_Back.api.model.exception.NotFoundException;
import T6Devs_Back.T6Devs_Back.api.model.mapper.SoftwareMapper;
import T6Devs_Back.T6Devs_Back.api.model.entity.Laboratory;
import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import T6Devs_Back.T6Devs_Back.api.model.entity.Software;
import T6Devs_Back.T6Devs_Back.api.model.entity.SoftwareUsage;
import T6Devs_Back.T6Devs_Back.api.model.repository.LaboratoryRepository;
import T6Devs_Back.T6Devs_Back.api.model.repository.SoftwareRepository;
import T6Devs_Back.T6Devs_Back.api.model.repository.SoftwareUsageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftwareService {

    private final SoftwareRepository softwareRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final SoftwareUsageRepository softwareUsageRepository;
    private final SoftwareMapper softwareMapper;
    private final ProfessorService professorService;

    @Transactional(readOnly = true)
    public List<SoftwareDTO> getAllDisponiveis() {
        return softwareRepository.findByDisponivelTrue().stream()
                .map(softwareMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SoftwareDTO> getAll() {
        return softwareRepository.findAll().stream()
                .map(softwareMapper::toDto)
                .toList();
    }

    @Transactional
    public SoftwareDTO create(SoftwareDTO softwareDTO) {
        Software software = softwareMapper.toEntity(softwareDTO);
        software.setDisponivel(true);
        Software savedSoftware = softwareRepository.save(software);
        return softwareMapper.toDto(savedSoftware);
    }

    @Transactional
    public SoftwareDTO update(Long id, SoftwareDTO softwareDTO) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Software não encontrado"));
        
        software.setNome(softwareDTO.getNome());
        software.setLink(softwareDTO.getLink());
        software.setVersao(softwareDTO.getVersao());
        software.setLivre(softwareDTO.getTipo() == SoftwareDTO.TipoSoftware.LIVRE);
        software.setDataSolicitacao(softwareDTO.getDataSolicitacao() != null 
            ? softwareDTO.getDataSolicitacao().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : null);
        software.setDisponivel(softwareDTO.isDisponivel());
        
        Software updatedSoftware = softwareRepository.save(software);
        return softwareMapper.toDto(updatedSoftware);
    }

    @Transactional
    public SoftwareDTO toggleAvailability(Long id) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Software não encontrado"));
        
        software.setDisponivel(!software.isDisponivel());
        Software updatedSoftware = softwareRepository.save(software);
        return softwareMapper.toDto(updatedSoftware);
    }

    @Transactional
    public void delete(Long id) {
        if (!softwareRepository.existsById(id)) {
            throw new NotFoundException("Software não encontrado");
        }
        softwareRepository.deleteById(id);
    }

    @Transactional
    public void removeSoftwareFromLab(Long softwareId, Long labId) {
        Software software = softwareRepository.findById(softwareId)
                .orElseThrow(() -> new NotFoundException("Software não encontrado"));
        
        Laboratory lab = laboratoryRepository.findById(labId)
                .orElseThrow(() -> new NotFoundException("Laboratório não encontrado"));
        
        if (!lab.getSoftwaresInstalados().contains(software)) {
            throw new IllegalStateException("Software não está instalado neste laboratório");
        }
        
        lab.getSoftwaresInstalados().remove(software);
        software.getLaboratorios().remove(lab);
        
        laboratoryRepository.save(lab);
        softwareRepository.save(software);

        if (lab.getSoftwaresInstalados().contains(software) || software.getLaboratorios().contains(lab)) {
            throw new RuntimeException("Falha ao remover o software do laboratório");
        }
    }

    @Transactional
    public void confirmUsage(Long softwareId, Authentication authentication) {
        Software software = softwareRepository.findById(softwareId)
                .orElseThrow(() -> new NotFoundException("Software não encontrado"));

        // Obter o email do usuário autenticado a partir do JWT
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");

        // Obter o professor autenticado
        Professor professor = professorService.getProfessorByEmail(email);

        SoftwareUsage usage = new SoftwareUsage();
        usage.setSoftware(software);
        usage.setProfessor(professor);
        usage.setDataConfirmacao(LocalDateTime.now());

        softwareUsageRepository.save(usage);
    }
}