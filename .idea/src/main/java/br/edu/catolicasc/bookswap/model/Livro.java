package br.edu.catolicasc.bookswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Exemplar físico disponível para troca (RF02).
 * Tabela: tb_livro
 */
@Entity
@Table(name = "tb_livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    /** Valores controlados: NOVO, SEMINOVO, MARCAS_USO (validado no DTO). */
    @Column(name = "estado_conservacao", nullable = false, length = 30)
    private String estadoConservacao;

    /** N:1 — cada livro possui exatamente um gênero (RN03). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genero_id", nullable = false)
    private Genero genero;

    /** N:1 — cada livro possui exatamente um proprietário (RN02). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leitor_id", nullable = false)
    private Leitor proprietario;

    protected Livro() {
        // exigido pelo JPA
    }

    public Livro(String titulo, String autor, String estadoConservacao, Genero genero, Leitor proprietario) {
        this.titulo = titulo;
        this.autor = autor;
        this.estadoConservacao = estadoConservacao;
        this.genero = genero;
        this.proprietario = proprietario;
    }

    public void atualizar(String titulo, String autor, String estadoConservacao, Genero genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.estadoConservacao = estadoConservacao;
        this.genero = genero;
    }

    /** Verifica se o livro pertence ao leitor informado. */
    public boolean pertenceA(Leitor leitor) {
        return leitor != null && proprietario != null
                && Objects.equals(proprietario.getId(), leitor.getId());
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEstadoConservacao() { return estadoConservacao; }
    public Genero getGenero() { return genero; }
    public Leitor getProprietario() { return proprietario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass());
    }
}
