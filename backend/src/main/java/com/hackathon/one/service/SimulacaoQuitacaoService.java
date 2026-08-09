package com.hackathon.one.service;

import com.hackathon.one.dto.SimulacaoQuitacaoRequest;
import com.hackathon.one.dto.SimulacaoQuitacaoResponse;
import com.hackathon.one.exception.ArquivoInvalidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Simulação de quitação de dívida.
 * Sem taxa de juros informada: cálculo simples (dívida / valor mensal).
 * Com taxa de juros informada: fórmula de Price (juros compostos, prestação fixa).
 */
@Service
@Slf4j
public class SimulacaoQuitacaoService {

    public SimulacaoQuitacaoResponse simular(SimulacaoQuitacaoRequest request) {
        BigDecimal divida = request.valorDivida();
        BigDecimal valorMensal = request.valorMensal();
        BigDecimal taxa = request.taxaJurosMensal();

        boolean comJuros = taxa != null && taxa.compareTo(BigDecimal.ZERO) > 0;

        int meses = comJuros
                ? calcularMesesComJuros(divida, valorMensal, taxa)
                : calcularMesesSemJuros(divida, valorMensal);

        BigDecimal valorTotalPago = valorMensal.multiply(BigDecimal.valueOf(meses));
        BigDecimal totalJurosPagos = valorTotalPago.subtract(divida).max(BigDecimal.ZERO);

        log.info("Simulação de quitação: dívida={} | valorMensal={} | taxa={} | meses={}",
                divida, valorMensal, taxa, meses);

        return new SimulacaoQuitacaoResponse(divida, valorMensal, taxa, meses, valorTotalPago, totalJurosPagos);
    }

    private int calcularMesesSemJuros(BigDecimal divida, BigDecimal valorMensal) {
        BigDecimal mesesExatos = divida.divide(valorMensal, 10, RoundingMode.CEILING);
        return mesesExatos.setScale(0, RoundingMode.CEILING).intValue();
    }

    // Fórmula de Price (juros compostos, prestação fixa):
    // n = -log(1 - (divida * taxa) / valorMensal) / log(1 + taxa)
    private int calcularMesesComJuros(BigDecimal divida, BigDecimal valorMensal, BigDecimal taxa) {
        double d = divida.doubleValue();
        double vm = valorMensal.doubleValue();
        double t = taxa.doubleValue();

        double jurosMensalDaDivida = d * t;

        if (vm <= jurosMensalDaDivida) {
            throw new ArquivoInvalidoException(
                    "O valor mensal informado não é suficiente para cobrir os juros da dívida — ela nunca seria quitada. " +
                    "Informe um valor mensal maior que " + BigDecimal.valueOf(jurosMensalDaDivida).setScale(2, RoundingMode.CEILING)
            );
        }

        double n = -Math.log(1 - (jurosMensalDaDivida / vm)) / Math.log(1 + t);

        return (int) Math.ceil(n);
    }
}