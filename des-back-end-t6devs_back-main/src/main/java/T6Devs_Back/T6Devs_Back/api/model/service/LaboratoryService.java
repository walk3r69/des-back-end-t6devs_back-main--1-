package T6Devs_Back.T6Devs_Back.api.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import T6Devs_Back.T6Devs_Back.api.dto.LaboratoryDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Laboratory;
import T6Devs_Back.T6Devs_Back.api.model.repository.LaboratoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;

    @Transactional(readOnly = true)
    public List<LaboratoryDTO> getAll() {
        return laboratoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LaboratoryDTO create(LaboratoryDTO laboratoryDTO) {
        Laboratory laboratory = convertToEntity(laboratoryDTO);
        Laboratory savedLaboratory = laboratoryRepository.save(laboratory);
        return convertToDTO(savedLaboratory);
    }

    @Transactional
    public void delete(Long id) {
        if (laboratoryRepository.existsById(id)) {
            laboratoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Laboratório não encontrado");
        }
    }

    private LaboratoryDTO convertToDTO(Laboratory laboratory) {
        List<Long> softwaresInstaladosIds = laboratory.getSoftwaresInstalados().stream()
                .map(software -> software.getId())
                .collect(Collectors.toList());
        return new LaboratoryDTO(
                laboratory.getId(),
                laboratory.getNome(),
                laboratory.getStatus(),
                softwaresInstaladosIds
        );
    }

    private Laboratory convertToEntity(LaboratoryDTO dto) {
        Laboratory laboratory = new Laboratory();
        laboratory.setNome(dto.getNome());
        laboratory.setStatus(dto.getStatus());
        // Não inicializamos a lista de softwares aqui, ela será gerenciada em outro momento
        return laboratory;
    }
}