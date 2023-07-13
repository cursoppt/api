package com.minsait.api.controller;

import com.minsait.api.controller.dto.ClienteRequest;
import com.minsait.api.controller.dto.ClienteResponse;
import com.minsait.api.controller.dto.MessageResponse;
import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name ="endpoints do curso de práticas tecnológicas")
public interface ApiSwagger {

    @Operation(summary = "Busca todos os clientes",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do cliente retornados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                }
    )
    public ResponseEntity<Page<ClienteResponse>> clienteFindAll(String nome, String endereco, int page, int pagesize);

    @Operation(summary = "Insere um novo cliente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente inserido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<ClienteResponse> insert(ClienteRequest request);

    @Operation(summary = "Atualiza um cliente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<ClienteResponse> update(ClienteRequest request);

    @Operation(summary = "Exclui um cliente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<MessageResponse> delete(Long id);

    @Operation(summary = "Busca um cliente pelo id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Dados do cliente retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<ClienteResponse> findById(Long id);
    
    @Operation(summary = "Busca todos os usuarios",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuario retornados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
            }
    )
    public ResponseEntity<Page<UsuarioResponse>> usuarioFindAll(String nome, String login, String email, int page, int pagesize);
    
    @Operation(summary = "Insere um novo usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario inserido com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
            }
    )
    public ResponseEntity<UsuarioResponse> insertUsuario(UsuarioRequest request);
    
    @Operation(summary = "Atualiza um usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
            }
    )
    public ResponseEntity<UsuarioResponse> updateUsuario(UsuarioRequest request);
    
    @Operation(summary = "Exclui um usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario excluído com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
            }
    )
    public ResponseEntity<MessageResponse> deleteUsuario(Long id);
    
    @Operation(summary = "Busca um usuario pelo id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Dados do cliente retornados com sucesso"),
                @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                @ApiResponse(responseCode = "500", description = "Erro interno"),
                @ApiResponse(responseCode = "403", description = "Acesso negado"),
            }
        )
        public ResponseEntity<UsuarioResponse> usuarioFindById(Long id);
}
