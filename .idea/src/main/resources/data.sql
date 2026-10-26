-- Dados de exemplo carregados ao iniciar a aplicação (perfil padrão, H2)

INSERT INTO tb_genero (nome) VALUES ('Ficção');
INSERT INTO tb_genero (nome) VALUES ('Fantasia');
INSERT INTO tb_genero (nome) VALUES ('Romance');
INSERT INTO tb_genero (nome) VALUES ('Biografia');
INSERT INTO tb_genero (nome) VALUES ('Suspense');
INSERT INTO tb_genero (nome) VALUES ('Literatura Científica');

INSERT INTO tb_ponto_encontro (nome, endereco, ativo) VALUES ('Biblioteca Municipal', 'Rua Exemplo, 100 - Centro, Jaraguá do Sul/SC', TRUE);
INSERT INTO tb_ponto_encontro (nome, endereco, ativo) VALUES ('Campus da Universidade', 'Avenida Exemplo, 500 - Jaraguá do Sul/SC', TRUE);
INSERT INTO tb_ponto_encontro (nome, endereco, ativo) VALUES ('Praça Central (desativado)', 'Praça Exemplo, s/n - Centro, Jaraguá do Sul/SC', FALSE);

INSERT INTO tb_leitor (nome, email, cidade) VALUES ('Ana Souza', 'ana.souza@email.com', 'Jaraguá do Sul');
INSERT INTO tb_leitor (nome, email, cidade) VALUES ('Bruno Lima', 'bruno.lima@email.com', 'Joinville');
INSERT INTO tb_leitor (nome, email, cidade) VALUES ('Carla Mendes', 'carla.mendes@email.com', 'Jaraguá do Sul');

INSERT INTO tb_livro (titulo, autor, estado_conservacao, genero_id, leitor_id) VALUES ('O Hobbit', 'J. R. R. Tolkien', 'SEMINOVO', 2, 1);
INSERT INTO tb_livro (titulo, autor, estado_conservacao, genero_id, leitor_id) VALUES ('Dom Casmurro', 'Machado de Assis', 'MARCAS_USO', 3, 1);
INSERT INTO tb_livro (titulo, autor, estado_conservacao, genero_id, leitor_id) VALUES ('O Silêncio dos Inocentes', 'Thomas Harris', 'NOVO', 5, 2);
INSERT INTO tb_livro (titulo, autor, estado_conservacao, genero_id, leitor_id) VALUES ('Cosmos', 'Carl Sagan', 'SEMINOVO', 6, 2);
INSERT INTO tb_livro (titulo, autor, estado_conservacao, genero_id, leitor_id) VALUES ('1984', 'George Orwell', 'MARCAS_USO', 1, 3);

-- Bruno (2) quer "O Hobbit" (1) da Ana e oferece "Cosmos" (4)
INSERT INTO tb_solicitacao_troca (data_solicitacao, status, livro_desejado_id, livro_oferecido_id, solicitante_id, ponto_encontro_id)
VALUES (CURRENT_DATE, 'PENDENTE', 1, 4, 2, 1);
