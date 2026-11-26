package br.com.neuraxi.view;

import br.com.neuraxi.controller.AgendamentoController;
import br.com.neuraxi.controller.PacienteController;
import br.com.neuraxi.controller.RegraNegocioException;
import br.com.neuraxi.model.Agendamento;
import br.com.neuraxi.model.Paciente;
import br.com.neuraxi.model.StatusAgendamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Tela (View) do CRUD de agendamentos. Não tem SQL: só conversa com os controllers.
public class AgendamentoPanel extends JPanel {

    private final AgendamentoController controller = new AgendamentoController();
    private final PacienteController pacienteController = new PacienteController();

    private final JComboBox<Paciente> cbPaciente = new JComboBox<>();
    private final JComboBox<String> cbServico = new JComboBox<>(new String[]{
            "Psicoterapia", "Avaliação psicológica", "Psicopedagogia", "Fonoaudiologia"});
    private final JTextField txtDataHora = new JTextField();
    private final JComboBox<StatusAgendamento> cbStatus = new JComboBox<>(StatusAgendamento.values());
    private final DefaultTableModel modelo =
            Estilo.modeloTabela("ID", "Paciente", "Serviço", "Data e hora", "Status");
    private final JTable tabela = new JTable(modelo);
    private int idSelecionado = -1;

    public AgendamentoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        cbServico.setEditable(true); // permite digitar outro serviço

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Paciente:"));
        form.add(cbPaciente);
        form.add(new JLabel("Serviço:"));
        form.add(cbServico);
        form.add(new JLabel("Data e hora (dd/mm/aaaa hh:mm):"));
        form.add(txtDataHora);
        form.add(new JLabel("Status:"));
        form.add(cbStatus);

        JButton btnAgendar = Estilo.botao("Agendar", Estilo.VERDE);
        JButton btnAtualizar = Estilo.botao("Atualizar", Estilo.AZUL);
        JButton btnExcluir = Estilo.botao("Excluir", Estilo.VERMELHO);
        JButton btnLimpar = Estilo.botao("Limpar", Estilo.CINZA);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnAgendar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        Estilo.tabela(tabela);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnAgendar.addActionListener(e -> agendar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        recarregar();
    }

    private void agendar() {
        try {
            controller.agendar((Paciente) cbPaciente.getSelectedItem(), servico(), txtDataHora.getText());
            Estilo.sucesso(this, "Agendamento criado com status \"pedido\".");
            limpar();
            carregarTabela();
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private void atualizar() {
        if (idSelecionado == -1) {
            Estilo.erro(this, "Selecione um agendamento na tabela.");
            return;
        }
        try {
            controller.atualizar(idSelecionado, (Paciente) cbPaciente.getSelectedItem(), servico(),
                    txtDataHora.getText(), (StatusAgendamento) cbStatus.getSelectedItem());
            Estilo.sucesso(this, "Agendamento atualizado.");
            limpar();
            carregarTabela();
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == -1) {
            Estilo.erro(this, "Selecione um agendamento na tabela.");
            return;
        }
        if (!Estilo.confirmar(this, "Excluir este agendamento?")) return;
        try {
            controller.excluir(idSelecionado);
            Estilo.sucesso(this, "Agendamento excluído.");
            limpar();
            carregarTabela();
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    // Chamado ao abrir a aba: atualiza a lista de pacientes e a tabela
    public void recarregar() {
        try {
            cbPaciente.removeAllItems();
            for (Paciente p : pacienteController.listar()) {
                cbPaciente.addItem(p);
            }
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
        carregarTabela();
    }

    private void carregarTabela() {
        try {
            modelo.setRowCount(0);
            for (Agendamento a : controller.listar()) {
                modelo.addRow(new Object[]{a.getId(), a.getPaciente().getNome(),
                        a.getServico(), a.getDataHora(), a.getStatus()});
            }
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        idSelecionado = (int) modelo.getValueAt(linha, 0);
        try {
            Agendamento a = controller.buscar(idSelecionado);
            for (int i = 0; i < cbPaciente.getItemCount(); i++) {
                if (cbPaciente.getItemAt(i).getId() == a.getPaciente().getId()) {
                    cbPaciente.setSelectedIndex(i);
                }
            }
            cbServico.setSelectedItem(a.getServico());
            txtDataHora.setText(a.getDataHora());
            cbStatus.setSelectedItem(a.getStatus());
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private String servico() {
        Object item = cbServico.getEditor().getItem();
        return item == null ? "" : item.toString();
    }

    private void limpar() {
        idSelecionado = -1;
        cbServico.setSelectedIndex(0);
        txtDataHora.setText("");
        cbStatus.setSelectedItem(StatusAgendamento.PEDIDO);
        tabela.clearSelection();
    }
}
