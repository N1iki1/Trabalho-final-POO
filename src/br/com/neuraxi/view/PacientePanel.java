package br.com.neuraxi.view;

import br.com.neuraxi.controller.PacienteController;
import br.com.neuraxi.controller.RegraNegocioException;
import br.com.neuraxi.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Tela (View) do CRUD de pacientes. Não tem SQL: só conversa com o controller.
public class PacientePanel extends JPanel {

    private final PacienteController controller = new PacienteController();

    private final JTextField txtNome = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final JTextField txtNascimento = new JTextField();
    private final DefaultTableModel modelo = Estilo.modeloTabela("ID", "Nome", "Telefone", "Nascimento");
    private final JTable tabela = new JTable(modelo);
    private int idSelecionado = -1;

    public PacientePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Telefone (com DDD):"));
        form.add(txtTelefone);
        form.add(new JLabel("Nascimento (dd/mm/aaaa):"));
        form.add(txtNascimento);

        JButton btnCadastrar = Estilo.botao("Cadastrar", Estilo.VERDE);
        JButton btnAtualizar = Estilo.botao("Atualizar", Estilo.AZUL);
        JButton btnExcluir = Estilo.botao("Excluir", Estilo.VERMELHO);
        JButton btnLimpar = Estilo.botao("Limpar", Estilo.CINZA);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnCadastrar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        Estilo.tabela(tabela);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> cadastrar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        carregarTabela();
    }

    private void cadastrar() {
        try {
            controller.cadastrar(txtNome.getText(), txtTelefone.getText(), txtNascimento.getText());
            Estilo.sucesso(this, "Paciente cadastrado.");
            limpar();
            carregarTabela();
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private void atualizar() {
        if (idSelecionado == -1) {
            Estilo.erro(this, "Selecione um paciente na tabela.");
            return;
        }
        try {
            controller.atualizar(idSelecionado, txtNome.getText(), txtTelefone.getText(), txtNascimento.getText());
            Estilo.sucesso(this, "Paciente atualizado.");
            limpar();
            carregarTabela();
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == -1) {
            Estilo.erro(this, "Selecione um paciente na tabela.");
            return;
        }
        if (!Estilo.confirmar(this, "Excluir este paciente?")) return;
        try {
            controller.excluir(idSelecionado);
            Estilo.sucesso(this, "Paciente excluído.");
            limpar();
            carregarTabela();
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    // READ: busca no banco (via controller) e mostra na tabela
    public void carregarTabela() {
        try {
            modelo.setRowCount(0);
            for (Paciente p : controller.listar()) {
                modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getTelefone(), p.getDataNascimento()});
            }
        } catch (RegraNegocioException ex) {
            Estilo.erro(this, ex.getMessage());
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        idSelecionado = (int) modelo.getValueAt(linha, 0);
        txtNome.setText((String) modelo.getValueAt(linha, 1));
        txtTelefone.setText((String) modelo.getValueAt(linha, 2));
        txtNascimento.setText((String) modelo.getValueAt(linha, 3));
    }

    private void limpar() {
        idSelecionado = -1;
        txtNome.setText("");
        txtTelefone.setText("");
        txtNascimento.setText("");
        tabela.clearSelection();
    }
}
