package com.hackathon.one.service;

import com.hackathon.one.domain.Transacao;
import com.hackathon.one.domain.Usuario;
import com.hackathon.one.dto.ProcessamentoLoteResponse;
import com.hackathon.one.exception.ResourceNotFoundException;
import com.hackathon.one.repository.TransacaoRepository;
import com.hackathon.one.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Processamento de transações em lote via upload de CSV.
 * Formato esperado do CSV: "descricao,valor" (com cabeçalho na primeira linha).
 * Linhas inválidas não interrompem o processamento das demais — são reportadas
 * individualmente no campo "erros" da resposta.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessamentoLoteService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ProcessamentoLoteResponse processar(MultipartFile arquivo, String emailUsuario) {
        log.info("Iniciando processamento de CSV em lote para usuário: {}", emailUsuario);

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", emailUsuario));

        List<String> erros = new ArrayList<>();
        int totalCriadas = 0;
        int numeroLinha = 0;

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(arquivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linha = leitor.readLine(); // primeira linha = cabeçalho, ignorada
            numeroLinha++;

            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;

                if (linha.isBlank()) {
                    continue; // pula linhas em branco silenciosamente
                }

                try {
                    Transacao transacao = parseLinha(linha, usuario);
                    transacaoRepository.save(transacao);
                    totalCriadas++;
                } catch (Exception e) {
                    String erro = String.format("Linha %d: %s", numeroLinha, e.getMessage());
                    erros.add(erro);
                    log.warn("Falha ao processar linha {} do CSV: {}", numeroLinha, e.getMessage());
                }
            }

        } catch (IOException e) {
            log.error("Erro ao ler arquivo CSV enviado por {}: ", emailUsuario, e);
            throw new RuntimeException("Não foi possível ler o arquivo CSV enviado.");
        }

        log.info("Processamento de CSV concluído para {}: {} criadas, {} falhas",
                emailUsuario, totalCriadas, erros.size());

        return new ProcessamentoLoteResponse(totalCriadas, erros.size(), erros);
    }

    // Converte uma linha "descricao,valor" em uma Transacao.
    // Lança exceção com mensagem clara se o formato estiver errado.
    private Transacao parseLinha(String linha, Usuario usuario) {
        String[] campos = linha.split(",", 2);

        if (campos.length != 2) {
            throw new IllegalArgumentException("Formato inválido, esperado 'descricao,valor'.");
        }

        String descricao = campos[0].trim();
        String valorTexto = campos[1].trim();

        if (descricao.isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode estar em branco.");
        }

        BigDecimal valor;
        try {
            valor = new BigDecimal(valorTexto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido: '" + valorTexto + "'.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }

        return Transacao.builder()
                .descricao(descricao)
                .valor(valor)
                .categoria("outros")
                .usuario(usuario)
                .build();
    }
}