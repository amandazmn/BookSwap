package br.edu.catolicasc.bookswap.repository;

import br.edu.catolicasc.bookswap.model.SolicitacaoTroca;
import br.edu.catolicasc.bookswap.model.StatusTroca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitacaoTrocaRepository extends JpaRepository<SolicitacaoTroca, Long> {

    List<SolicitacaoTroca> findBySolicitanteId(Long leitorId);

    List<SolicitacaoTroca> findByLivroDesejadoProprietarioId(Long leitorId);

    /** Indica se o livro aparece em alguma solicitação (como desejado ou oferecido). */
    @Query("""
            select case when count(s) > 0 then true else false end from SolicitacaoTroca s
            where s.livroDesejado.id = :livroId or s.livroOferecido.id = :livroId
            """)
    boolean livroPossuiSolicitacoes(@Param("livroId") Long livroId);

    /** Indica se o livro participa de alguma solicitação com o status informado. */
    @Query("""
            select case when count(s) > 0 then true else false end from SolicitacaoTroca s
            where s.status = :status
              and (s.livroDesejado.id = :livroId or s.livroOferecido.id = :livroId)
            """)
    boolean livroPossuiSolicitacaoComStatus(@Param("livroId") Long livroId,
                                            @Param("status") StatusTroca status);
}
