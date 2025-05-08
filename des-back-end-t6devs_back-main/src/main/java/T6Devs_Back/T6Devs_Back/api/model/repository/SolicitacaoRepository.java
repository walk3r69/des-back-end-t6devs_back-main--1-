package T6Devs_Back.T6Devs_Back.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;
import T6Devs_Back.T6Devs_Back.api.model.entity.Solicitacao;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByStatus(String status);
    List<Solicitacao> findByProfessor(Professor professor);
}