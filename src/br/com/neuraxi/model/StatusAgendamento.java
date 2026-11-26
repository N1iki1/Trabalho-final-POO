package br.com.neuraxi.model;

// Valores permitidos para o status do agendamento (igual ao CHECK do banco)
public enum StatusAgendamento {
    PEDIDO("pedido"),
    CONFIRMADO("confirmado"),
    REALIZADO("realizado"),
    CANCELADO("cancelado");

    private final String valorBanco;

    StatusAgendamento(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public String getValorBanco() {
        return valorBanco;
    }

    public static StatusAgendamento deValorBanco(String valor) {
        for (StatusAgendamento s : values()) {
            if (s.valorBanco.equals(valor)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + valor);
    }

    @Override
    public String toString() {
        return valorBanco;
    }
}
