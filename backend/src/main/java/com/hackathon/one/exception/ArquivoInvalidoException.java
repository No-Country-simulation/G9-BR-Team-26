package com.hackathon.one.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Lançada quando o arquivo enviado (ex: upload de CSV) é inválido:
// ausente, extensão errada, ou tamanho acima do permitido.
// Resulta automaticamente em resposta HTTP 400 Bad Request.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArquivoInvalidoException extends RuntimeException {

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }
}