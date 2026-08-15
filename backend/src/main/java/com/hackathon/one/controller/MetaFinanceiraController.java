package com.hackathon.one.controller;

import com.hackathon.one.dto.ErrorResponse;
import com.hackathon.one.dto.MetaFinanceiraRequest;
import com.hackathon.one.dto.MetaFinanceiraResponse;
import com.hackathon.one.service.MetaFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/metas")
@RequiredArgsConstructor
@Tag(name = "Metas Financeiras", description = "Criação e acompanhamento de metas de endividamento")
public class MetaFinanceiraController {

    private final MetaFinanceiraService metaFinanceiraService;

    @Operation(
            summary = "Criar meta financeira",
            description = "Cria uma nova meta de endividamento para o usuário autenticado. O endividamento no momento " +
                    "da criação é capturado como referência inicial, usado para calcular o progresso de forma estável.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meta criada com sucesso",
                    content = @Content(schema = @Schema(implementation = MetaFinanceiraResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: data alvo no passado, endividamento negativo)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<MetaFinanceiraResponse> criar(
            @Valid @RequestBody MetaFinanceiraRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        MetaFinanceiraResponse response = metaFinanceiraService.criar(request, emailUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Listar metas e progresso",
            description = "Retorna todas as metas do usuário autenticado, com o progresso calculado em relação à análise " +
                    "financeira mais recente. O progresso é null se o usuário ainda não tiver feito nenhuma análise.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de metas retornada com sucesso (pode ser vazia)"),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<MetaFinanceiraResponse>> listar(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        List<MetaFinanceiraResponse> response = metaFinanceiraService.listar(emailUsuario);
        return ResponseEntity.ok(response);
    }
}