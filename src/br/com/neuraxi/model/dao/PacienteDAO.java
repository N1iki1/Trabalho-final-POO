package br.com.neuraxi.model.dao;

import br.com.neuraxi.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Todo o SQL da tabela paciente fica aqui (padrão DAO)
public class PacienteDAO {

    // CREATE
    public void inserir(Paciente p) throws SQLException {
        String sql = "INSERT INTO paciente (nome, telefone, data_nascimento) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getTelefone());
            ps.setString(3, p.getDataNascimento());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ (todos)
    public List<Paciente> listar() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente ORDER BY nome";
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
    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    // UPDATE
    public void atualizar(Paciente p) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, telefone = ?, data_nascimento = ? WHERE id_paciente = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getTelefone());
            ps.setString(3, p.getDataNascimento());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Converte uma linha do banco em objeto
    private Paciente montar(ResultSet rs) throws SQLException {
        return new Paciente(
                rs.getInt("id_paciente"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("data_nascimento"));
    }
}
