package T6Devs_Back.T6Devs_Back.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import T6Devs_Back.T6Devs_Back.api.model.entity.RelatoProblema;
import T6Devs_Back.T6Devs_Back.api.model.entity.Solicitacao;

import java.util.List;

@Repository
public interface RelatoProblemaRepository extends JpaRepository<RelatoProblema, Long> {
    List<RelatoProblema> findBySolicitacao(Solicitacao solicitacao);
}