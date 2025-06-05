package com.marllon.vieira.vergili.catalogo_financeiro.restController;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.security.UsuarioAuthenticationSecurity;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Portal de Login")
public class AuthenticationRestController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private UsuarioAuthenticationSecurity authenticationSecurity;


    @PostMapping(value = "/RealizarLogin/{email}/{senha}")
    public void autenticarUsuarioComSenha(@RequestParam String senha){

        //if (usuarioSecurity.autenticarUsuario)
    }

    @PutMapping(value = "/resetarSenha/{senha}")
    public ResponseEntity<UsuarioResponse> resetarSenhaUsuario(@RequestParam String senha){

        //if (usuarioSecurity.autenticarUsuario)
        return null;
    }
}

