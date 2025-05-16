INSERT INTO pagamentos (id, valor, data, descricao) VALUES
(1, 1000.00, '2025-01-01', 'pagamento teste'),
(2, 150.75, '2025-01-05', 'Reembolso de despesas'),
(3, 500.00, '2025-01-10', 'Parcela do empréstimo'),
(4, 80.00, '2025-01-15', 'Pagamento de transporte'),
(5, 1200.50, '2025-01-20', 'Pagamento do aluguel'),
(6, 3200.00, '2025-01-25', 'Salário do mês'),
(7, 45.90, '2025-01-26', 'Compra de materiais'),
(8, 99.99, '2025-01-27', 'Assinatura de serviço'),
(9, 400.00, '2025-01-28', 'Manutenção do veículo'),
(10, 600.00, '2025-01-29', 'Pagamento extra ao fornecedor'),
(11, 250.00, '2025-01-30', 'Compra de equipamentos'),
(12, 100.00, '2025-01-31', 'Doação beneficente'),
(13, 750.00, '2025-02-01', 'Pagamento parcial do empréstimo'),
(14, 890.00, '2025-02-02', 'Pagamento de aluguel de fevereiro'),
(15, 2300.00, '2025-02-05', 'Pagamento de comissão'),
(16, 180.00, '2025-02-07', 'Mensalidade da academia'),
(17, 20.00, '2025-02-10', 'Compra de aplicativo'),
(18, 133.33, '2025-02-15', 'Pagamento parcial de dívida'),
(19, 2000.00, '2025-02-20', 'Pagamento para prestador de serviço'),
(20, 35.75, '2025-02-25', 'Compra de café e lanche');


--VALORES DE OUTRAS ENTIDADES PARA TESTE
INSERT INTO contas(id,nome,saldo,tipo_conta) values(1,'contateste', 1000, CONTA_CORRENTE);
INSERT INTO historico_transacoes(id,valor,data,descricao) values(1,1000,'2000-01-01','historico teste');
INSERT INTO usuarios(id,nome,email,senha,telefone) values(1,'teste','teste@email.com','Teste123','(11)11111-1111');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (1, 'RECEITA', 'PRESENTE');