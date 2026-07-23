package com.hackathon.one.controller;

import com.hackathon.one.dto.ClassificacaoResponse;
import com.hackathon.one.dto.TransacaoRequest;
import com.hackathon.one.service.ClassificacaoService;
import io.swagger.v3.oas.annotations.Operation;
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
            description = "Recebe descrição e valor de uma transação e devolve a categoria classificada",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/classificar")
    public ResponseEntity<ClassificacaoResponse> classificar(
            @Valid @RequestBody TransacaoRequest request
    ) {
        ClassificacaoResponse response = classificacaoService.classificar(request);
        return ResponseEntity.ok(response);
    }
}