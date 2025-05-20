-- Despesas
INSERT INTO historico_transacoes (id, valor, data, descricao, tipos_categorias) VALUES
(1,  250.00, '2025-01-10', 'Conta de Luz Janeiro', 'DESPESA'),
(2,  100.00, '2025-01-11', 'Conta de Água', 'DESPESA'),
(3,  120.00, '2025-01-12', 'Internet Mensal', 'DESPESA'),
(4,  850.00, '2025-01-15', 'Fatura do cartão Nubank', 'DESPESA'),
(5,  200.00, '2025-01-20', 'Cinema e Pizza', 'DESPESA'),
(6,  300.00, '2025-01-21', 'Compras do mês', 'DESPESA'),
(7,  1200.00, '2025-01-25', 'Aluguel do apartamento', 'DESPESA'),
(8,  500.00, '2025-01-27', 'Parcela do empréstimo', 'DESPESA');

-- Receitas
INSERT INTO historico_transacoes (id, valor, data, descricao, tipos_categorias) VALUES
(9,  3000.00, '2025-01-05', 'Salário de Janeiro', 'RECEITA'),
(10,  150.00, '2025-01-10', 'Presente de aniversário', 'RECEITA'),
(11,  120.00, '2025-01-18', 'Dividendos ações', 'RECEITA'),
(12,  80.00, '2025-01-22', 'Renda CDB', 'RECEITA'),
(13,  900.00, '2025-01-28', 'Aluguel recebido', 'RECEITA'),
(14,  2500.00, '2025-12-25', 'Conta de energia paga', 'RECEITA');