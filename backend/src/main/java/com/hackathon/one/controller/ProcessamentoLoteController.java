package com.hackathon.one.controller;

import com.hackathon.one.dto.ProcessamentoLoteResponse;
import com.hackathon.one.service.ProcessamentoLoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Processamento de transações em lote via CSV")
public class ProcessamentoLoteController {

    private final ProcessamentoLoteService processamentoLoteService;

    // ─────────────────────────────────────────────────
    //  POST /transacoes/lote
    // ─────────────────────────────────────────────────

    @Operation(
            summary = "Processar transações em lote via CSV",
            description = "Recebe um arquivo CSV (colunas: descricao,valor) e cria uma transação para cada linha válida",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/lote")
    public ResponseEntity<ProcessamentoLoteResponse> processarLote(
            @RequestParam("arquivo") MultipartFile arquivo,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        ProcessamentoLoteResponse response = processamentoLoteService.processar(arquivo, emailUsuario);
        return ResponseEntity.ok(response);
    }
}