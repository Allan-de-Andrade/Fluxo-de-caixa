package com.soverteria.frimel.modelos.dto;

import java.math.BigDecimal;

public class DespesaDTO {

        private Long id;

        private String  descricao;

        private BigDecimal valor;

        private String data;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescricao() {
                return descricao;
            }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
}
