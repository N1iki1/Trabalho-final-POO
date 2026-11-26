package br.com.neuraxi.controller;

import br.com.neuraxi.model.Agendamento;
import br.com.neuraxi.model.Paciente;
import br.com.neuraxi.model.StatusAgendamento;
import br.com.neuraxi.model.dao.AgendamentoDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

// Regras de negócio de Agendamento: fica entre a View e o DAO
public class AgendamentoController {

    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    public Agendamento agendar(Paciente paciente, String servico, String dataHora) throws RegraNegocioException {
        Agendamento a = validar(0, paciente, servico, dataHora, StatusAgendamento.PEDIDO);
        // Regra: agendamento novo não pode ser no passado
        if (Validador.dataHora(dataHora, "Data e hora").isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Não é possível agendar numa data que já passou.");
        }
        try {
            agendamentoDAO.inserir(a);
            return a;
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao agendar: " + e.getMessage());
        }
    }

    public List<Agendamento> listar() throws RegraNegocioException {
        try {
            return agendamentoDAO.listar();
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao listar agendamentos: " + e.getMessage());
        }
    }

    public Agendamento buscar(int id) throws RegraNegocioException {
        try {
            Agendamento a = agendamentoDAO.buscarPorId(id);
            if (a == null) {
                throw new RegraNegocioException("Agendamento não encontrado.");
            }
            return a;
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao buscar agendamento: " + e.getMessage());
        }
    }

    public void atualizar(int id, Paciente paciente, String servico, String dataHora,
                          StatusAgendamento status) throws RegraNegocioException {
        buscar(id); // garante que existe
        Agendamento a = validar(id, paciente, servico, dataHora, status);
        try {
            agendamentoDAO.atualizar(a);
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao atualizar agendamento: " + e.getMessage());
        }
    }

    public void excluir(int id) throws RegraNegocioException {
        buscar(id);
        try {
            agendamentoDAO.excluir(id);
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao excluir agendamento: " + e.getMessage());
        }
    }

    private Agendamento validar(int id, Paciente paciente, String servico, String dataHora,
                                StatusAgendamento status) throws RegraNegocioException {
        if (paciente == null) {
            throw new RegraNegocioException("Selecione um paciente. Cadastre um antes, se precisar.");
        }
        Validador.obrigatorio(servico, "Serviço");
        Validador.obrigatorio(dataHora, "Data e hora");
        Validador.dataHora(dataHora, "Data e hora");
        if (status == null) {
            throw new RegraNegocioException("Selecione o status.");
        }
        return new Agendamento(id, paciente, servico.trim(), dataHora.trim(), status);
    }
}
