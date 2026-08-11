package com.hackathon.one.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Lançada quando uma entrada é inválida de forma que não se encaixa em @Valid.
// Usos atuais: upload de CSV (ausente, extensão errada, tamanho acima do permitido)
// e simulação de quitação (combinação de valores matematicamente inválida).
// Resulta automaticamente em resposta HTTP 400 Bad Request.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArquivoInvalidoException extends RuntimeException {

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }
}