package br.com.neuraxi.view;

import javax.swing.*;
import java.awt.*;

// Janela principal com cabeçalho e as duas abas de CRUD
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Neuraxi | Gestão de Pacientes e Agendamentos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("  NEURAXI  |  Atendimento inteligente para clínicas");
        titulo.setOpaque(true);
        titulo.setBackground(Estilo.ROXO);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setPreferredSize(new Dimension(0, 50));

        PacientePanel pacientes = new PacientePanel();
        AgendamentoPanel agendamentos = new AgendamentoPanel();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Pacientes", pacientes);
        abas.addTab("Agendamentos", agendamentos);
        // Ao trocar de aba, recarrega os dados do banco
        abas.addChangeListener(e -> {
            if (abas.getSelectedComponent() == agendamentos) {
                agendamentos.recarregar();
            } else {
                pacientes.carregarTabela();
            }
        });

        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }
}
