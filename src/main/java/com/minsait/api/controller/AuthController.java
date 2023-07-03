package com.minsait.api.controller;

import com.minsait.api.controller.dto.GetTokenRequest;
import com.minsait.api.controller.dto.GetTokenResponse;
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

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/get-token")
    public ResponseEntity<GetTokenResponse> getToken(@RequestBody GetTokenRequest request){
    	
    	final var usuarioEncontrado = usuarioRepository.findByLogin(request.getUserName());
		
		if (usuarioEncontrado != null){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			if (encoder.matches(request.getPassword(), usuarioEncontrado.getSenha())) {
				final ArrayList<String> permissions = new ArrayList<>();
				final String[] list = usuarioEncontrado.getPermissoes().split(",");
				for (String string : list) {
					permissions.add(string);
				}

				final var token = jwtUtil.generateToken(usuarioEncontrado.getLogin(), permissions, Integer.parseInt(usuarioEncontrado.getId().toString()));
				return new ResponseEntity<>(GetTokenResponse.builder()
                        .accessToken(token)
                        .build(), HttpStatus.OK);	
        	}else{
                return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.UNAUTHORIZED);
            } 
		}else{
			return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.NOT_FOUND);
		}
    }
}
