package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioImpl implements UsuariosService {


    @Override
    public UsuarioResponse criarUsuario(UsuarioRequest usuario, ContaUsuarioRequest conta) {
        return null;
    }

    @Override
    public Usuario encontrarUsuarioPorId(Long id) {
        return null;
    }

    @Override
    public List<UsuarioResponse> buscarUsuarioPorNome(String nome) {
        return List.of();
    }

    @Override
    public List<UsuarioResponse> buscarPorContaId(Long contaId) {
        return List.of();
    }

    @Override
    public Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable) {
        return null;
    }

    @Override
    public void deletarUsuario(Long id) {

    }

    @Override
    public List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId) {
        return List.of();
    }

    @Override
    public boolean usuarioTemContaTipo(Long usuarioId, TiposContas tipoConta) {
        return false;
    }

    @Override
    public boolean existePelaId(Long id) {
        return false;
    }

    @Override
    public boolean existeUsuarioIgual(UsuarioRequest usuario) {
        return false;
    }
}
