package br.com.neuraxi.controller;

import br.com.neuraxi.model.Paciente;
import br.com.neuraxi.model.dao.AgendamentoDAO;
import br.com.neuraxi.model.dao.PacienteDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Regras de negócio de Paciente: fica entre a View e o DAO
public class PacienteController {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    public Paciente cadastrar(String nome, String telefone, String nascimento) throws RegraNegocioException {
        Paciente p = validar(0, nome, telefone, nascimento);
        try {
            pacienteDAO.inserir(p);
            return p;
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }

    public List<Paciente> listar() throws RegraNegocioException {
        try {
            return pacienteDAO.listar();
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao listar pacientes: " + e.getMessage());
        }
    }

    public Paciente buscar(int id) throws RegraNegocioException {
        try {
            Paciente p = pacienteDAO.buscarPorId(id);
            if (p == null) {
                throw new RegraNegocioException("Paciente não encontrado.");
            }
            return p;
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao buscar paciente: " + e.getMessage());
        }
    }

    public void atualizar(int id, String nome, String telefone, String nascimento) throws RegraNegocioException {
        buscar(id); // garante que existe
        Paciente p = validar(id, nome, telefone, nascimento);
        try {
            pacienteDAO.atualizar(p);
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    public void excluir(int id) throws RegraNegocioException {
        try {
            // Regra: paciente com agendamento não pode ser excluído
            int qtd = agendamentoDAO.contarPorPaciente(id);
            if (qtd > 0) {
                throw new RegraNegocioException(
                        "Não é possível excluir: o paciente tem " + qtd + " agendamento(s).");
            }
            pacienteDAO.excluir(id);
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao excluir paciente: " + e.getMessage());
        }
    }

    private Paciente validar(int id, String nome, String telefone, String nascimento) throws RegraNegocioException {
        Validador.obrigatorio(nome, "Nome");
        Validador.obrigatorio(telefone, "Telefone");
        Validador.obrigatorio(nascimento, "Data de nascimento");
        String tel = Validador.telefone(telefone);
        LocalDate data = Validador.data(nascimento, "Data de nascimento");
        if (data.isAfter(LocalDate.now())) {
            throw new RegraNegocioException("A data de nascimento não pode ser no futuro.");
        }
        return new Paciente(id, nome.trim(), tel, nascimento.trim());
    }
}
