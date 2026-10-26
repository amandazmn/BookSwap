package br.edu.catolicasc.bookswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Usuário cadastrado na plataforma (RF01).
 * Tabela: tb_leitor
 */
@Entity
@Table(name = "tb_leitor")
public class Leitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 50)
    private String cidade;

    /** 1:N — um leitor possui zero ou vários livros (lado inverso). */
    @OneToMany(mappedBy = "proprietario")
    private List<Livro> livros = new ArrayList<>();

    /** 1:N — um leitor realiza zero ou várias solicitações (lado inverso). */
    @OneToMany(mappedBy = "solicitante")
    private List<SolicitacaoTroca> solicitacoes = new ArrayList<>();

    protected Leitor() {
        // exigido pelo JPA
    }

    public Leitor(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    /** Atualiza os dados cadastrais do leitor. */
    public void atualizarDados(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCidade() { return cidade; }
    public List<Livro> getLivros() { return livros; }
    public List<SolicitacaoTroca> getSolicitacoes() { return solicitacoes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leitor other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass());
    }
}
