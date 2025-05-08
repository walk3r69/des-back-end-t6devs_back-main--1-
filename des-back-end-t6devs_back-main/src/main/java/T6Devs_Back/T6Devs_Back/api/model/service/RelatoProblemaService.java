package T6Devs_Back.T6Devs_Back.api.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import T6Devs_Back.T6Devs_Back.api.dto.RelatoProblemaDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.RelatoProblema;
import T6Devs_Back.T6Devs_Back.api.model.entity.Solicitacao;
import T6Devs_Back.T6Devs_Back.api.model.repository.RelatoProblemaRepository;
import T6Devs_Back.T6Devs_Back.api.model.repository.SolicitacaoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatoProblemaService {

    private final RelatoProblemaRepository relatoRepository;
    private final SolicitacaoRepository solicitacaoRepository;

    @Transactional(readOnly = true)
    public List<RelatoProblemaDTO> getAll() {
        return relatoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RelatoProblemaDTO> getBySolicitacao(Long solicitacaoId) {
        Optional<Solicitacao> optionalSolicitacao = solicitacaoRepository.findById(solicitacaoId);
        if (optionalSolicitacao.isPresent()) {
            return relatoRepository.findBySolicitacao(optionalSolicitacao.get()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Solicitação não encontrada");
    }

    @Transactional
    public RelatoProblemaDTO create(RelatoProblemaDTO relatoDTO) {
        Optional<Solicitacao> optionalSolicitacao = solicitacaoRepository.findById(relatoDTO.getSolicitacaoId());
        if (optionalSolicitacao.isPresent()) {
            Solicitacao solicitacao = optionalSolicitacao.get();
            if (!solicitacao.getStatus().equals("FINALIZADA")) {
                throw new RuntimeException("A solicitação precisa estar finalizada para relatar problemas");
            }
            // Validação: dataRelato não pode ser futura
            if (relatoDTO.getDataRelato().isAfter(LocalDate.now())) {
                throw new RuntimeException("A data do relato não pode ser futura");
            }
            RelatoProblema relato = new RelatoProblema();
            relato.setSolicitacao(solicitacao);
            relato.setDescricao(relatoDTO.getDescricao());
            relato.setDataRelato(relatoDTO.getDataRelato());
            RelatoProblema savedRelato = relatoRepository.save(relato);
            return convertToDTO(savedRelato);
        }
        throw new RuntimeException("Solicitação não encontrada");
    }

    private RelatoProblemaDTO convertToDTO(RelatoProblema relato) {
        return new RelatoProblemaDTO(
                relato.getId(),
                relato.getSolicitacao().getId(),
                relato.getDescricao(),
                relato.getDataRelato()
        );
    }
}