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
 * Classificação literária de um livro (RF03).
 * Tabela: tb_genero
 */
@Entity
@Table(name = "tb_genero")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    /** 1:N — um gênero classifica zero ou vários livros (lado inverso). */
    @OneToMany(mappedBy = "genero")
    private List<Livro> livros = new ArrayList<>();

    protected Genero() {
        // exigido pelo JPA
    }

    public Genero(String nome) {
        this.nome = nome;
    }

    public void atualizar(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public List<Livro> getLivros() { return livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genero other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass());
    }
}
