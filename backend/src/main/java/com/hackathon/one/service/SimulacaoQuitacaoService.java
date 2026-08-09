package com.hackathon.one.service;

import com.hackathon.one.dto.SimulacaoQuitacaoRequest;
import com.hackathon.one.dto.SimulacaoQuitacaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class SimulacaoQuitacaoService {

    public SimulacaoQuitacaoResponse simular(SimulacaoQuitacaoRequest request) {
        BigDecimal divida = request.valorDivida();
        BigDecimal valorMensal = request.valorMensal();

        BigDecimal mesesExatos = divida.divide(valorMensal, 10, RoundingMode.CEILING);
        int meses = mesesExatos.setScale(0, RoundingMode.CEILING).intValue();

        BigDecimal valorTotalPago = valorMensal.multiply(BigDecimal.valueOf(meses));

        log.info("Simulação de quitação: dívida={} | valorMensal={} | meses={}", divida, valorMensal, meses);

        return new SimulacaoQuitacaoResponse(divida, valorMensal, meses, valorTotalPago);
    }
}