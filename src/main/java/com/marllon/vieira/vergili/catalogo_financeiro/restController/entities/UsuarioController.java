package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/usuario")
@Tag(name = "Métodos da entidade usuario, sem associação (apenas para teste)")
public class UsuarioController {



    @Autowired
    private UsuarioService usuarioService;



    @GetMapping(value = "/encontrarUsuarioPorId/{id}")
    public Usuario encontrarUsuarioPorId(@PathVariable Long id){
        return usuarioService.encontrarUsuarioPorId(id);

    }

    @GetMapping(value = "/encontrarUsuarioPorNome/{nome}")
    public Usuario encontrarUsuarioPorNome(@PathVariable String nome){
        return usuarioService.encontrarUsuarioPorNome(nome);
    }


    @GetMapping(value = "/obterTodosUsuarios")
    public List<Usuario> obterTodosUsuarios(){
        return usuarioService.encontrarTodosUsuarios();
    }



    @PostMapping(value = "/criarUsuario")
    public Usuario criarNovoUsuario(@RequestBody UsuarioRequest usuario){
        return usuarioService.criarNovoUsuario(usuario);
    }


    @PutMapping(value = "/atualizarUsuario/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest usuario){
        return usuarioService.atualizarDadosUsuario(id, usuario);
    }


    @DeleteMapping(value = "/removerUsuarioPorId/{id}")
    public Usuario removerUsuarioPelaId(@PathVariable Long id){
        return usuarioService.removerUsuarioPorId(id);
    }

}