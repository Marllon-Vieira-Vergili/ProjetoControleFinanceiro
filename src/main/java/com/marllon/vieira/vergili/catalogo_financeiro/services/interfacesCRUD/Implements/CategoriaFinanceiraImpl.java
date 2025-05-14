package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaFinanceiraImpl implements CategoriaFinanceiraService {
    @Override
    public CategoriaFinanceiraResponse criarCategoriaFinanceira(CategoriaFinanceiraRequest request) {
        return null;
    }

    @Override
    public CategoriaFinanceira encontrarCategoriaPorId(Long id) {

        return null;
    }

    @Override
    public List<CategoriaFinanceiraResponse> encontrarCategoriaCriadaPeloSubTipo(Long id, SubTipoCategoria subTipo) {
        return List.of();
    }

    @Override
    public Page<CategoriaFinanceiraResponse> encontrarTodasCategorias(Pageable pageable) {
        return null;
    }

    @Override
    public CategoriaFinanceiraResponse atualizarUmaCategoriaCriada(Long idCategoria, CategoriaFinanceiraRequest novosDados) {
        return null;
    }

    @Override
    public void deletarCategoria(Long id) {

    }

    @Override
    public boolean seCategoriaForDespesa() {
        return false;
    }

    @Override
    public boolean seCategoriaForReceita() {
        return false;
    }

    @Override
    public boolean tipoCategoriaExiste(TiposCategorias tipoCategoria) {
        return false;
    }



    @Override
    public boolean jaExisteUmaCategoriaIgual(CategoriaFinanceira dadosCategoria) {
        return false;
    }
}
