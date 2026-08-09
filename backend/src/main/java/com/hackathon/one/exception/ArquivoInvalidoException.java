package com.hackathon.one.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Lançada quando uma entrada é inválida de forma que não se encaixa em @Valid
// (ex: arquivo com extensão errada, ou uma combinação de valores matematicamente
// inválida, como na simulação de quitação). Resulta em HTTP 400 Bad Request.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArquivoInvalidoException extends RuntimeException {

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }
}