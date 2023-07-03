package com.minsait.api.controller;

import com.minsait.api.controller.dto.GetTokenRequest;
import com.minsait.api.controller.dto.GetTokenResponse;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.sicurity.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/get-token")
    public ResponseEntity<GetTokenResponse> getToken(@RequestBody GetTokenRequest request){
        if(request.getPassword().equals("12345") && request.getUserName().equals("root")){
            final ArrayList<String> permissions = new ArrayList<>();
            permissions.add("LEITURA_USUARIO");
            permissions.add("ESCRITA_USUARIO");

            UsuarioEntity usuarioEncontrado = usuarioRepository.findByLogin(request.getUserName());

            final var token =jwtUtil.generateToken("admin", permissions, 5);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            encoder.matches(request.getPassword(), usuarioEncontrado.getSenha());
            return new ResponseEntity<>(GetTokenResponse.builder()
                    .accessToken(token)
                    .build(), HttpStatus.OK);
        }else{
            UsuarioEntity usuarioEncontrado = usuarioRepository.findByLogin(request.getUserName());

            final var usuarioEntityFound = usuarioRepository.findById(usuarioEncontrado.getId());
            if(usuarioEntityFound.isEmpty()){
                return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.NOT_FOUND);
            }
            final ArrayList<String> permissions = new ArrayList<>(List.of(usuarioEncontrado.getPermissoes().split(",")));

            final var token =jwtUtil.generateToken(usuarioEncontrado.getLogin(), permissions, Math.toIntExact(usuarioEncontrado.getId()));
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            encoder.matches(request.getPassword(), usuarioEncontrado.getSenha());
            return new ResponseEntity<>(GetTokenResponse.builder()
                    .accessToken(token)
                    .build(), HttpStatus.OK);
        }
    }
}
