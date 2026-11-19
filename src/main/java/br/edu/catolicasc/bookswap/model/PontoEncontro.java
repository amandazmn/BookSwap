package br.edu.catolicasc.bookswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Local aprovado para a realização das trocas (RF04).
 * Tabela: tb_ponto_encontro
 */
@Entity
@Table(name = "tb_ponto_encontro")
public class PontoEncontro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    /** Permite desativar o local sem apagar o histórico de solicitações (RN10). */
    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    protected PontoEncontro() {
        // exigido pelo JPA
    }

    public PontoEncontro(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
        this.ativo = Boolean.TRUE;
    }

    public void atualizar(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public void desativar() {
        this.ativo = Boolean.FALSE;
    }

    public boolean isAtivo() {
        return Boolean.TRUE.equals(ativo);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public Boolean getAtivo() { return ativo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PontoEncontro other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass());
    }
}
