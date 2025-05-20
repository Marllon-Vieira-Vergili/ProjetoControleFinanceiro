INSERT INTO pagamentos (id, valor, data, descricao, tipos_categorias) VALUES
(1, 1000.00, '2025-01-01', 'pagamento teste', 'RECEITA'),
(2, 150.75, '2025-01-05', 'Reembolso de despesas', 'DESPESA'),
(3, 500.00, '2025-01-10', 'Parcela do empréstimo', 'DESPESA'),
(4, 80.00, '2025-01-15', 'Pagamento de transporte', 'DESPESA'),
(5, 1200.50, '2025-01-20', 'Pagamento do aluguel', 'DESPESA'),
(6, 3200.00, '2025-01-25', 'Salário do mês', 'RECEITA'),
(7, 45.90, '2025-01-26', 'Compra de materiais', 'DESPESA'),
(8, 99.99, '2025-01-27', 'Assinatura de serviço', 'DESPESA'),
(9, 400.00, '2025-01-28', 'Manutenção do veículo', 'DESPESA'),
(10, 600.00, '2025-01-29', 'Pagamento extra ao fornecedor', 'DESPESA'),
(11, 250.00, '2025-01-30', 'Compra de equipamentos', 'DESPESA'),
(12, 100.00, '2025-01-31', 'Doação beneficente', 'DESPESA'),
(13, 750.00, '2025-02-01', 'Pagamento parcial do empréstimo', 'DESPESA'),
(14, 890.00, '2025-02-02', 'Pagamento de aluguel de fevereiro', 'DESPESA'),
(15, 2300.00, '2025-02-05', 'Pagamento de comissão', 'RECEITA'),
(16, 180.00, '2025-02-07', 'Mensalidade da academia', 'DESPESA'),
(17, 20.00, '2025-02-10', 'Compra de aplicativo', 'DESPESA'),
(18, 133.33, '2025-02-15', 'Pagamento parcial de dívida', 'DESPESA'),
(19, 2000.00, '2025-02-20', 'Pagamento para prestador de serviço', 'DESPESA'),
(20, 35.75, '2025-02-25', 'Compra de café e lanche', 'DESPESA');

-- Valores de outras entidades para teste
INSERT INTO contas(id, nome, saldo, tipo_conta) VALUES (1, 'contateste', 1000, 'CONTA_CORRENTE');
INSERT INTO historico_transacoes(id, valor, data, descricao,tipos_categorias) VALUES (1, 1000, '2000-01-01', 'teste', 'RECEITA');
INSERT INTO usuarios(id, nome, email, senha, telefone) VALUES (1, 'teste', 'teste@email.com', 'Teste123', '(11)11111-1111');
INSERT INTO categoria_das_contas (id, tipos_categorias, subtipo_categoria) VALUES (1, 'RECEITA', 'PRESENTE');