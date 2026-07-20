package com.hackathon.one.controller;

import com.hackathon.one.dto.UserResponse;
import com.hackathon.one.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints relacionados ao perfil do usuário")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ─────────────────────────────────────────
    //  GET /usuarios/me
    // ─────────────────────────────────────────

    // Retorna o perfil do usuário autenticado.
    // Esse é o endpoint recomendado para que o frontend exiba dados do usuário logado.
    // @param userDetails usuário autenticado, injetado automaticamente pelo Spring Security
    // @return 200 OK com o DTO do usuário (sem senha)
    @Operation(summary = "Meu perfil", description = "Retorna as informações do usuário autenticado", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/me")
    public ResponseEntity<UserResponse> meuPerfil(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        UserResponse response = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────
    //  GET /usuarios/all
    // ─────────────────────────────────────────

    // Lista todos os usuários cadastrados; é uma rota administrativa.
    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários cadastrados (somente para administradores)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> listarTodos() {
        List<UserResponse> response = usuarioService.listarTodos();
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────
    //  GET /usuarios/{id}
    // ─────────────────────────────────────────

    // Retorna os dados públicos de um usuário pelo seu ID numérico.
    // A senha nunca é incluída na resposta.
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados públicos de um usuário pelo identificador", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarPorId(@PathVariable Long id) {
        UserResponse response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────
    //  DELETE /usuarios/{id}
    // ─────────────────────────────────────────

    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema pelo identificador", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
