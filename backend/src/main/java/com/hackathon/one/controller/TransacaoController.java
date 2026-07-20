package com.hackathon.one.controller;

import com.hackathon.one.dto.TransacaoRequest;
import com.hackathon.one.dto.TransacaoResponse;
import com.hackathon.one.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Gestão de transações financeiras do usuário")
public class TransacaoController {

    private final TransacaoService transacaoService;

    // ─────────────────────────────────────────
    //  POST /transacoes
    // ─────────────────────────────────────────

    // Cria uma nova transação para o usuário autenticado.
    // @param request corpo com descricao e valor
    
    @Operation(summary = "Criar transação", description = "Registra uma nova transação para o usuário autenticado", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(
            @Valid @RequestBody TransacaoRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        TransacaoResponse response = transacaoService.criar(request, emailUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────
    //  GET /transacoes
    // ─────────────────────────────────────────

    // Lista todas as transações do usuário autenticado, da mais recente para a mais antiga.
    // @param userDetails usuário autenticado extraído automaticamente do JWT pelo Spring Security
    @Operation(summary = "Listar transações", description = "Retorna todas as transações cadastradas pelo usuário autenticado", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<TransacaoResponse>> listarMinhas(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        List<TransacaoResponse> response = transacaoService.listarMinhas(emailUsuario);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar transação", description = "Atualiza os dados de uma transação do usuário autenticado", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TransacaoRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        TransacaoResponse response = transacaoService.atualizar(id, request, emailUsuario);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir transação", description = "Remove uma transação do usuário autenticado", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        transacaoService.deletar(id, emailUsuario);
        return ResponseEntity.noContent().build();
    }
}
