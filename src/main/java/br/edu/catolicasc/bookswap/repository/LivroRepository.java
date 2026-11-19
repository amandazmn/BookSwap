package br.edu.catolicasc.bookswap.repository;

import br.edu.catolicasc.bookswap.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long>, JpaSpecificationExecutor<Livro> {

    List<Livro> findByProprietarioId(Long leitorId);
}
