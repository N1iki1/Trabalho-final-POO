package br.com.neuraxi.model;

// Entidade Agendamento (tabela agendamento), ligada a um Paciente
public class Agendamento {
    private int id;
    private Paciente paciente;
    private String servico;
    private String dataHora; // dd/mm/aaaa hh:mm
    private StatusAgendamento status = StatusAgendamento.PEDIDO;

    public Agendamento() {
    }

    public Agendamento(int id, Paciente paciente, String servico, String dataHora, StatusAgendamento status) {
        this.id = id;
        this.paciente = paciente;
        this.servico = servico;
        this.dataHora = dataHora;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getServico() { return servico; }
    public void setServico(String servico) { this.servico = servico; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
}
