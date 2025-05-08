package T6Devs_Back.T6Devs_Back.api.model.repository;

import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByEmail(String email);
}