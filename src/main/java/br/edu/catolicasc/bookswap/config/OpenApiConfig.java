package br.edu.catolicasc.bookswap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookswapOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookSwap API")
                        .description("API para troca de livros entre leitores: cadastro de leitores, livros, gêneros, pontos de encontro e solicitações de troca.")
                        .version("0.0.1"));
    }
}
