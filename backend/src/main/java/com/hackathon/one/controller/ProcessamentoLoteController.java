package com.hackathon.one.controller;

import com.hackathon.one.dto.ErrorResponse;
import com.hackathon.one.dto.ProcessamentoLoteResponse;
import com.hackathon.one.exception.ArquivoInvalidoException;
import com.hackathon.one.service.ProcessamentoLoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private static final long TAMANHO_MAXIMO_BYTES = 1_048_576L; // 1 MB

    private final ProcessamentoLoteService processamentoLoteService;

    @Operation(
            summary = "Processar transações em lote via CSV",
            description = "Recebe um arquivo CSV (colunas: descricao,valor) e cria uma transação para cada linha válida. " +
                    "Limite: 1MB, apenas arquivos .csv. Linhas inválidas não interrompem o processamento das demais — " +
                    "são reportadas individualmente no campo 'erros' da resposta.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arquivo processado (mesmo que algumas linhas tenham falhado — ver campo 'erros')",
                    content = @Content(schema = @Schema(implementation = ProcessamentoLoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Arquivo ausente, extensão diferente de .csv, ou tamanho acima de 1MB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token de autenticação ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/lote")
    public ResponseEntity<ProcessamentoLoteResponse> processarLote(
            @RequestParam("arquivo") MultipartFile arquivo,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        validarArquivo(arquivo);
        String emailUsuario = userDetails.getUsername();
        ProcessamentoLoteResponse response = processamentoLoteService.processar(arquivo, emailUsuario);
        return ResponseEntity.ok(response);
    }

    // Validações de entrada, antes de processar qualquer linha do arquivo.
    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoInvalidoException("Nenhum arquivo enviado.");
        }
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || !nomeOriginal.toLowerCase().endsWith(".csv")) {
            throw new ArquivoInvalidoException("Apenas arquivos .csv são aceitos.");
        }
        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw new ArquivoInvalidoException("Arquivo excede o tamanho máximo permitido (1MB).");
        }
    }
}