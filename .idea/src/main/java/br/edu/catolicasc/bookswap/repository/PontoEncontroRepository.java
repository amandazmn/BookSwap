package br.edu.catolicasc.bookswap.repository;

import br.edu.catolicasc.bookswap.model.PontoEncontro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoEncontroRepository extends JpaRepository<PontoEncontro, Long> {

    List<PontoEncontro> findByAtivoTrue();
}
