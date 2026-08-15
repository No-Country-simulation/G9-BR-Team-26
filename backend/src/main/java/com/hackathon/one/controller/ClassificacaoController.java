package com.hackathon.one.controller;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.ErrorResponse;
import com.hackathon.one.dto.TransacaoRequest;
import com.hackathon.one.service.ClassificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Classificação automática de transações financeiras")
public class ClassificacaoController {

    private final ClassificacaoService classificacaoService;

    // ─────────────────────────────────────────────────
    //  POST /transacoes/classificar
    // ─────────────────────────────────────────────────

    // Classifica isoladamente a categoria de uma transação, sem persistir.
    // NOTA: classificação MOCKADA por palavras-chave (ver comentário no ClassificacaoService).
    @Operation(
            summary = "Classificar transação",
            description = "Recebe descrição e valor de uma transação e devolve a categoria classificada com o nível de confiança. " +
                    "Este endpoint apenas classifica — não persiste a transação (para isso, use POST /transacoes).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação classificada com sucesso",
                    content = @Content(schema = @Schema(implementation = ClassificacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: descrição em branco)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/classificar")
    public ResponseEntity<ClassificacaoResponse> classificar(
            @Valid @RequestBody TransacaoRequest request
    ) {
        ClassificacaoResponse response = classificacaoService.classificar(request);
        return ResponseEntity.ok(response);
    }
}