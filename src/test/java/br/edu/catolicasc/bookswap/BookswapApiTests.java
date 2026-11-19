package br.edu.catolicasc.bookswap;

import br.edu.catolicasc.bookswap.model.Genero;
import br.edu.catolicasc.bookswap.model.Leitor;
import br.edu.catolicasc.bookswap.model.Livro;
import br.edu.catolicasc.bookswap.repository.GeneroRepository;
import br.edu.catolicasc.bookswap.repository.LeitorRepository;
import br.edu.catolicasc.bookswap.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes funcionais previstos na seção 7.1 do documento.
 * Usam os dados de exemplo de data.sql:
 * leitores 1 (Ana), 2 (Bruno), 3 (Carla); livros 1-2 da Ana, 3-4 do Bruno, 5 da Carla;
 * ponto 3 inativo; solicitação 1 PENDENTE (Bruno quer o livro 1 e oferece o 4).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookswapApiTests {

    @Autowired private MockMvc mvc;
    @Autowired private LeitorRepository leitorRepository;
    @Autowired private GeneroRepository generoRepository;
    @Autowired private LivroRepository livroRepository;

    private static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Test
    void persisteLivroComRelacionamentos() {
        Leitor leitor = leitorRepository.save(new Leitor("Teste", "teste@email.com", "Joinville"));
        Genero genero = generoRepository.save(new Genero("Poesia"));
        Livro livro = livroRepository.save(new Livro("Livro de Teste", "Autor", "NOVO", genero, leitor));

        Livro salvo = livroRepository.findById(livro.getId()).orElseThrow();
        assertThat(salvo.getProprietario().getId()).isEqualTo(leitor.getId());
        assertThat(salvo.getGenero().getNome()).isEqualTo("Poesia");
    }

    @Test
    void cadastraLeitorValido() throws Exception {
        mvc.perform(post("/api/v1/leitores").contentType(JSON)
                        .content("{\"nome\":\"Diego\",\"email\":\"diego@email.com\",\"cidade\":\"Blumenau\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void rejeitaEmailDuplicado() throws Exception {
        mvc.perform(post("/api/v1/leitores").contentType(JSON)
                        .content("{\"nome\":\"Outra Ana\",\"email\":\"ana.souza@email.com\",\"cidade\":\"Joinville\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void rejeitaDadosInvalidos() throws Exception {
        mvc.perform(post("/api/v1/leitores").contentType(JSON)
                        .content("{\"nome\":\"\",\"email\":\"nao-e-email\",\"cidade\":\"X\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes", hasSize(2)));
    }

    @Test
    void rejeitaLivroComReferenciaInexistente() throws Exception {
        mvc.perform(post("/api/v1/livros").contentType(JSON)
                        .content("{\"titulo\":\"X\",\"autor\":\"Y\",\"estadoConservacao\":\"NOVO\",\"generoId\":999,\"proprietarioId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void filtraLivrosPorGenero() throws Exception {
        mvc.perform(get("/api/v1/livros").param("genero", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("O Hobbit"));
    }

    @Test
    void criaSolicitacaoPendente() throws Exception {
        mvc.perform(post("/api/v1/trocas").contentType(JSON)
                        .content("{\"solicitanteId\":3,\"livroDesejadoId\":2,\"livroOferecidoId\":5,\"pontoEncontroId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.dataSolicitacao").exists());
    }

    @Test
    void rejeitaSolicitacaoDeLivroProprio() throws Exception {
        mvc.perform(post("/api/v1/trocas").contentType(JSON)
                        .content("{\"solicitanteId\":1,\"livroDesejadoId\":2,\"livroOferecidoId\":1,\"pontoEncontroId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaLivroOferecidoDeOutroLeitor() throws Exception {
        mvc.perform(post("/api/v1/trocas").contentType(JSON)
                        .content("{\"solicitanteId\":3,\"livroDesejadoId\":1,\"livroOferecidoId\":3,\"pontoEncontroId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaPontoInativo() throws Exception {
        mvc.perform(post("/api/v1/trocas").contentType(JSON)
                        .content("{\"solicitanteId\":3,\"livroDesejadoId\":2,\"livroOferecidoId\":5,\"pontoEncontroId\":3}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void somenteProprietarioDecideSolicitacao() throws Exception {
        mvc.perform(patch("/api/v1/trocas/1/status").contentType(JSON)
                        .content("{\"status\":\"ACEITA\",\"leitorId\":3}"))
                .andExpect(status().isForbidden());

        mvc.perform(patch("/api/v1/trocas/1/status").contentType(JSON)
                        .content("{\"status\":\"ACEITA\",\"leitorId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACEITA"));

        // após a troca aceita, "O Hobbit" deixa de aparecer como disponível
        mvc.perform(get("/api/v1/livros").param("titulo", "hobbit"))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
