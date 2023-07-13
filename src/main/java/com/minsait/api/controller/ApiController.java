package com.minsait.api.controller;

import com.minsait.api.controller.dto.ClienteRequest;
import com.minsait.api.controller.dto.ClienteResponse;
import com.minsait.api.controller.dto.MessageResponse;
import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;
import com.minsait.api.repository.ClienteEntity;
import com.minsait.api.repository.ClienteRepository;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ApiController implements ApiSwagger{

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@PreAuthorize("hasAuthority('LEITURA_CLIENTE')")
	@GetMapping("/cliente")
	public ResponseEntity<Page<ClienteResponse>> clienteFindAll(@RequestParam(required = false) String nome,
																@RequestParam(required = false) String endereco,
																@RequestParam(required = false, defaultValue = "0") int page,
																@RequestParam(required = false, defaultValue = "10") int pageSize) {
		final var clienteEntity = new ClienteEntity();
		clienteEntity.setEndereco(endereco);
		clienteEntity.setNome(nome);
		Pageable pageable = PageRequest.of(page, pageSize);

		final Page<ClienteEntity> clienteEntityListPage = clienteRepository.findAll(clienteEntity.clienteEntitySpecification(), pageable);
		final  Page<ClienteResponse> clienteResponseList = ObjectMapperUtil.mapAll(clienteEntityListPage, ClienteResponse.class);
		return ResponseEntity.ok(clienteResponseList);
	}

	@PreAuthorize("hasAuthority('ESCRITA_CLIENTE')")
	@PostMapping("/cliente")
	public ResponseEntity<ClienteResponse> insert(@RequestBody ClienteRequest request){

		final var clienteEntity = ObjectMapperUtil.map(request, ClienteEntity.class);
		final var clienteInserted = clienteRepository.save(clienteEntity);
		final var clienteResponse = ObjectMapperUtil.map(clienteInserted, ClienteResponse.class);

		return new ResponseEntity<>(clienteResponse, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ESCRITA_CLIENTE')")
	@PutMapping("/cliente")
	public ResponseEntity<ClienteResponse> update(@RequestBody ClienteRequest request){
		final var clienteEntity = ObjectMapperUtil.map(request, ClienteEntity.class);
		final var clienteEntityFound = clienteRepository.findById(clienteEntity.getId());
		if(clienteEntityFound.isEmpty()){
			return new ResponseEntity<>(new ClienteResponse(), HttpStatus.NOT_FOUND);
		}

		final var clienteUpdated = clienteRepository.save(clienteEntity);
		final var clienteResponse = ObjectMapperUtil.map(clienteUpdated, ClienteResponse.class);

		return new ResponseEntity<>(clienteResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ESCRITA_CLIENTE')")
	@DeleteMapping("/cliente/{id}")
	public ResponseEntity<MessageResponse> delete(@PathVariable Long id){
		final var clienteEntityFound = clienteRepository.findById(id);
		if(clienteEntityFound.isPresent()){
			clienteRepository.delete(clienteEntityFound.get());
		}else{
			return new ResponseEntity<>(MessageResponse.builder()
					.message("Cliente não encontrado!")
					.date(LocalDateTime.now())
					.error(false)
					.build(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(MessageResponse.builder()
				.message("OK")
				.date(LocalDateTime.now())
				.error(false)
				.build(), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('LEITURA_CLIENTE')")
	@GetMapping("/cliente/{id}")
	public ResponseEntity<ClienteResponse> findById(@PathVariable Long id){
		final var clienteEntity = clienteRepository.findById(id);
		ClienteResponse clienteResponse = new ClienteResponse();

		if (clienteEntity.isPresent()){
			clienteResponse = ObjectMapperUtil.map(clienteEntity.get(), ClienteResponse.class);
		}else{
			return new ResponseEntity<>(clienteResponse, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(clienteResponse, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('LEITURA_USUARIO')")
	@GetMapping("/usuario")
	public ResponseEntity<Page<UsuarioResponse>> usuarioFindAll(@RequestParam(required = false) String nome,
																@RequestParam(required = false) String login,
																@RequestParam(required = false) String email,
																@RequestParam(required = false, defaultValue = "0") int page,
																@RequestParam(required = false, defaultValue = "10") int pageSize) {
		final var usuarioEntity = new UsuarioEntity();
		usuarioEntity.setNome(nome);
	    usuarioEntity.setLogin(login);
	    usuarioEntity.setEmail(email);

	    Pageable pageable = PageRequest.of(page, pageSize);

	    final Page<UsuarioEntity> usuarioEntityListPage = usuarioRepository.findAll(
	                usuarioEntity.usuarioEntitySpecification(), pageable);
	    final Page<UsuarioResponse> usuarioResponseList = ObjectMapperUtil.mapAll(usuarioEntityListPage,
	                UsuarioResponse.class);
	    return ResponseEntity.ok(usuarioResponseList);
	}
	
	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@PostMapping("/usuario")
	public ResponseEntity<UsuarioResponse> insertUsuario(@RequestBody UsuarioRequest request){

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	    final var usuarioEntity = ObjectMapperUtil.map(request, UsuarioEntity.class);

	    final var senhaEncoded = passwordEncoder.encode(usuarioEntity.getSenha());
	    usuarioEntity.setSenha(senhaEncoded);

	    final var usuarioInserted = usuarioRepository.save(usuarioEntity);
	    final var usuarioResponse = ObjectMapperUtil.map(usuarioInserted, UsuarioResponse.class);

	    return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@PutMapping("/usuario")
	public ResponseEntity<UsuarioResponse> updateUsuario(@RequestBody UsuarioRequest request){
		  final var usuarioEntity = ObjectMapperUtil.map(request, UsuarioEntity.class);
	      final var usuarioEntityFound = usuarioRepository.findById(usuarioEntity.getId());
	        
	      if (usuarioEntityFound.isPresent()) {
	    	  BeanUtils.copyProperties(usuarioEntity, usuarioEntityFound.get(), "senha");
	    	  
	    	  if (Objects.nonNull(usuarioEntity.getSenha()) && !usuarioEntity.getSenha().isBlank()) {
	    		  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	              String senhaEncoded = encoder.encode(usuarioEntity.getSenha());
	              usuarioEntityFound.get().setSenha(senhaEncoded);
	          }

	          final var usuarioUpdated = usuarioRepository.save(usuarioEntityFound.get());
	          final var usuarioResponse = ObjectMapperUtil.map(usuarioUpdated, UsuarioResponse.class);

	          return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	      } else {
	            return new ResponseEntity<>(new UsuarioResponse(), HttpStatus.NOT_FOUND);
	      }
	}
	
	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@DeleteMapping("/usuario/{id}")
	public ResponseEntity<MessageResponse> deleteUsuario(@PathVariable Long id){
		final var usuarioEntityFound = usuarioRepository.findById(id);
		if(usuarioEntityFound.isPresent()){
			usuarioRepository.delete(usuarioEntityFound.get());
		}else{
			return new ResponseEntity<>(MessageResponse.builder()
					.message("Usuário não encontrado!")
					.date(LocalDateTime.now())
					.error(false)
					.build(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(MessageResponse.builder()
				.message("OK")
				.date(LocalDateTime.now())
				.error(false)
				.build(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('LEITURA_USUARIO')")
	@GetMapping("/usuario/{id}")
	public ResponseEntity<UsuarioResponse> usuarioFindById(@PathVariable Long id){
		final var usuarioEntity = usuarioRepository.findById(id);
		UsuarioResponse usuarioResponse = new UsuarioResponse();

		if (usuarioEntity.isPresent()){
			usuarioResponse = ObjectMapperUtil.map(usuarioEntity.get(), UsuarioResponse.class);
		}else{
			return new ResponseEntity<>(usuarioResponse, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}
	
}