package br.com.neuraxi.model.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Responsável por abrir a conexão com o SQLite e criar as tabelas
public class ConexaoBD {

    private static final String URL = "jdbc:sqlite:neuraxi.db";
    private static final String SCRIPT = "database/neuraxi.sql";

    public static Connection getConexao() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // No SQLite a chave estrangeira precisa ser ligada em cada conexão
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    // Executa o script .sql na primeira vez (usa CREATE TABLE IF NOT EXISTS)
    public static void inicializarBanco() {
        try {
            String script = Files.readString(Path.of(SCRIPT));
            try (Connection conn = getConexao(); Statement st = conn.createStatement()) {
                for (String comando : script.split(";")) {
                    String sql = removerComentarios(comando);
                    if (!sql.isBlank()) {
                        st.execute(sql);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o banco: " + e.getMessage(), e);
        }
    }

    private static String removerComentarios(String sql) {
        StringBuilder sb = new StringBuilder();
        for (String linha : sql.split("\n")) {
            int i = linha.indexOf("--");
            sb.append(i >= 0 ? linha.substring(0, i) : linha).append("\n");
        }
        return sb.toString().trim();
    }
}
