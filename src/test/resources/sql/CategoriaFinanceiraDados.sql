--Este arquivo SQL é um arquivo para instanciar objetos do tipo Categoria, para testes

-- DESPESAS
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (1, 'DESPESA', 'CONTA_LUZ');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (2, 'DESPESA', 'CONTA_AGUA');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (3, 'DESPESA', 'CONTA_INTERNET');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (4, 'DESPESA', 'CARTAO_CREDITO');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (5, 'DESPESA', 'CONTA_TELEFONE');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (6, 'DESPESA', 'IMPOSTOS');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (7, 'DESPESA', 'LAZER');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (8, 'DESPESA', 'COMBUSTIVEL');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (9, 'DESPESA', 'ALIMENTACAO');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (10, 'DESPESA', 'DESPESA_ALUGUEL');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (11, 'DESPESA', 'EMPRESTIMOS');

-- RECEITAS
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (20, 'RECEITA', 'SALARIO');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (21, 'RECEITA', 'PRESENTE');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (22, 'RECEITA', 'HERANCA');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (23, 'RECEITA', 'DIVIDENDOS');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (24, 'RECEITA', 'RENDA_FIXA');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (25, 'RECEITA', 'RENDA_ALUGUEL');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (26, 'RECEITA', 'OUTROS');


--DELETAR
DELETE FROM categoria_das_contas;


-- INSERÇÂO DE UM VALOR DAS OUTRAS ENTIDADES(PARA TESTE DE ASSOCIAÇÂO)
INSERT INTO pagamentos(id,valor,data,descricao) values(1,1000,'2000-01-01','pagamento teste');
INSERT INTO contas(id,nome,saldo,tipo_conta) values(1,'contateste', 1000, CONTA_CORRENTE);
INSERT INTO historico_transacoes(id,valor,data,descricao) values(1,1000,'2000-01-01','historico teste');
INSERT INTO usuarios(id,nome,email,senha,telefone) values(1,'teste','teste@email.com','Teste123','(11)11111-1111');
