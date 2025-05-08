package T6Devs_Back.T6Devs_Back.api.model.service;

import T6Devs_Back.T6Devs_Back.api.dto.ProfessorDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import T6Devs_Back.T6Devs_Back.api.model.mapper.ProfessorMapper;
import T6Devs_Back.T6Devs_Back.api.model.repository.ProfessorRepository;
import T6Devs_Back.T6Devs_Back.api.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ProfessorMapper professorMapper;

    public List<ProfessorDTO> getAll() {
        return professorRepository.findAll().stream()
                .map(professorMapper::toDto)
                .toList();
    }

    public ProfessorDTO create(ProfessorDTO dto) {
        Professor professor = professorMapper.toEntity(dto);
        professor = professorRepository.save(professor);
        return professorMapper.toDto(professor);
    }

    public void delete(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));
        professorRepository.delete(professor);
    }

    public Professor getProfessorByEmail(String email) {
        return professorRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado para o email: " + email));
    }
}