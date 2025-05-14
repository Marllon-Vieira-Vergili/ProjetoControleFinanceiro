package com.marllon.vieira.vergili.catalogo_financeiro.service;

import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.CategoriaFinanceiraImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class TiposCategoriaTestService {

    @MockitoBean
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;
    // Mock para o Spring injetar no serviço real

    @Autowired
    private CategoriaFinanceiraImpl categoriaFinanceiraService;
    // Injetando a classe de serviço real com o repositório mockado

    // seus testes aqui


    @Test
    @DisplayName("TestandoSeMetodoCriarTipoCategoriaEstaCriando")
    public void testEstaCriandoTipoCategoria(){
        CategoriaFinanceira novaCategoria = new CategoriaFinanceira();
        novaCategoria.setTiposCategorias();
    }
}

