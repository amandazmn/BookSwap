package br.edu.catolicasc.bookswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Proposta de troca entre dois leitores (RF05).
 * Tabela: tb_solicitacao_troca
 */
@Entity
@Table(name = "tb_solicitacao_troca")
public class SolicitacaoTroca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;

    /** Persistido como texto (PENDENTE, ACEITA, RECUSADA). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTroca status;

    /** N:1 — livro que o solicitante deseja receber. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "livro_desejado_id", nullable = false)
    private Livro livroDesejado;

    /** N:1 — livro oferecido pelo solicitante. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "livro_oferecido_id", nullable = false)
    private Livro livroOferecido;

    /** N:1 — leitor que iniciou a solicitação. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Leitor solicitante;

    /** N:1 — local escolhido para a troca. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ponto_encontro_id", nullable = false)
    private PontoEncontro pontoEncontro;

    protected SolicitacaoTroca() {
        // exigido pelo JPA
    }

    public SolicitacaoTroca(Leitor solicitante, Livro livroDesejado, Livro livroOferecido, PontoEncontro pontoEncontro) {
        this.solicitante = solicitante;
        this.livroDesejado = livroDesejado;
        this.livroOferecido = livroOferecido;
        this.pontoEncontro = pontoEncontro;
    }

    /** Preenche a data e o status inicial no momento da criação (RN06). */
    @PrePersist
    void prePersist() {
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDate.now();
        }
        if (status == null) {
            status = StatusTroca.PENDENTE;
        }
    }

    public boolean isPendente() {
        return status == null || status == StatusTroca.PENDENTE;
    }

    public void aceitar() {
        exigirPendente();
        this.status = StatusTroca.ACEITA;
    }

    public void recusar() {
        exigirPendente();
        this.status = StatusTroca.RECUSADA;
    }

    private void exigirPendente() {
        if (!isPendente()) {
            throw new IllegalStateException("Somente solicitações PENDENTES podem ser aceitas ou recusadas.");
        }
    }

    public Long getId() { return id; }
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public StatusTroca getStatus() { return status; }
    public Livro getLivroDesejado() { return livroDesejado; }
    public Livro getLivroOferecido() { return livroOferecido; }
    public Leitor getSolicitante() { return solicitante; }
    public PontoEncontro getPontoEncontro() { return pontoEncontro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolicitacaoTroca other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass());
    }
}
