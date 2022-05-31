package com.soverteria.frimel.modelos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Saldo {

    private BigDecimal valor;
    private LocalDateTime dateTime;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
