package com.minsait.api.controller;

import com.minsait.api.controller.dto.GetTokenRequest;
import com.minsait.api.controller.dto.GetTokenResponse;
import com.minsait.api.controller.dto.UsuarioRequest;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/get-token")
    public ResponseEntity<GetTokenResponse> getToken(@RequestBody GetTokenRequest request){
    	 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    	 
         Optional<UsuarioEntity> usuarioEntity = Optional.ofNullable(usuarioRepository.findByLogin(request.getUserName()));

         if (usuarioEntity.isPresent()) {
             UsuarioEntity usuario = usuarioEntity.get();
             boolean isIdenticalPassword = encoder.matches(request.getPassword(), usuario.getSenha());

             if (isIdenticalPassword) {
                 int userId = Math.toIntExact(usuario.getId());
                 String[] usuarioPermissions = usuario.getPermissoes().split(",");
                 ArrayList<String> permissions = new ArrayList<>(List.of(usuarioPermissions));

                 String token = jwtUtil.generateToken(usuario.getLogin(), permissions, userId);

                 GetTokenResponse response = GetTokenResponse.builder()
                         .accessToken(token)
                         .build();

                 return new ResponseEntity<>(response, HttpStatus.OK);
             } else {
                 return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.UNAUTHORIZED);
             }
         } else {
             return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.UNAUTHORIZED);
         }
    }
}
