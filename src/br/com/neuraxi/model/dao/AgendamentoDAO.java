package br.com.neuraxi.model.dao;

import br.com.neuraxi.model.Agendamento;
import br.com.neuraxi.model.Paciente;
import br.com.neuraxi.model.StatusAgendamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Todo o SQL da tabela agendamento fica aqui (padrão DAO)
public class AgendamentoDAO {

    // Consulta base com JOIN para trazer o paciente junto
    private static final String SELECT_BASE =
            "SELECT a.id_agendamento, a.servico, a.data_hora, a.status, "
          + "p.id_paciente, p.nome, p.telefone, p.data_nascimento "
          + "FROM agendamento a "
          + "INNER JOIN paciente p ON p.id_paciente = a.id_paciente ";

    // CREATE
    public void inserir(Agendamento a) throws SQLException {
        String sql = "INSERT INTO agendamento (id_paciente, servico, data_hora, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getPaciente().getId());
            ps.setString(2, a.getServico());
            ps.setString(3, a.getDataHora());
            ps.setString(4, a.getStatus().getValorBanco());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ (todos)
    public List<Agendamento> listar() throws SQLException {
        List<Agendamento> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY a.id_agendamento";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    // READ (um)
    public Agendamento buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE a.id_agendamento = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    // UPDATE
    public void atualizar(Agendamento a) throws SQLException {
        String sql = "UPDATE agendamento SET id_paciente = ?, servico = ?, data_hora = ?, status = ? "
                   + "WHERE id_agendamento = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getPaciente().getId());
            ps.setString(2, a.getServico());
            ps.setString(3, a.getDataHora());
            ps.setString(4, a.getStatus().getValorBanco());
            ps.setInt(5, a.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM agendamento WHERE id_agendamento = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Conta quantos agendamentos um paciente tem (usado na regra de exclusão)
    public int contarPorPaciente(int idPaciente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM agendamento WHERE id_paciente = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private Agendamento montar(ResultSet rs) throws SQLException {
        Paciente p = new Paciente(
                rs.getInt("id_paciente"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("data_nascimento"));
        return new Agendamento(
                rs.getInt("id_agendamento"),
                p,
                rs.getString("servico"),
                rs.getString("data_hora"),
                StatusAgendamento.deValorBanco(rs.getString("status")));
    }
}
