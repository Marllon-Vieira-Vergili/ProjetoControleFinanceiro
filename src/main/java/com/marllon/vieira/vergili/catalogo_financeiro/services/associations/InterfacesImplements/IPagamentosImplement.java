package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.*;

@Service
public class IPagamentosImplement implements IPagamentos {

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private TransacoesService transacoesService;

    @Autowired
    private CategoriaFinanceiraService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    public PagamentoAssociationResponse criarEAssociarPagamento(PagamentoAssociationRequest novoPagamento) {
        return null;
    }


    @Override
    public PagamentoAssociationResponse encontrarPagamentoAssociadoPorId(Long id) {
        return null;
    }

    @Override
    public List<PagamentoAssociationResponse> encontrarTodosPagamentosAssociados() {
        return List.of();
    }

    @Override
    public PagamentoAssociationResponse atualizarPagamentoAssociado(Long id, PagamentoAssociationRequest
            pagamentoAtualizado) {
        return null;
    }

    @Override
    public PagamentoAssociationResponse removerPagamentoAssociadoPelaId(Long id) {
        return null;
    }
}

