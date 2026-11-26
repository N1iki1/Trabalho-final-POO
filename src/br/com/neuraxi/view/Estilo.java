package br.com.neuraxi.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Cores, botões e mensagens usados em todas as telas
public class Estilo {

    public static final Color ROXO = new Color(76, 52, 160);
    public static final Color VERDE = new Color(46, 139, 87);
    public static final Color AZUL = new Color(41, 98, 255);
    public static final Color VERMELHO = new Color(200, 50, 50);
    public static final Color CINZA = new Color(110, 110, 110);

    public static JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setPreferredSize(new Dimension(110, 32));
        return b;
    }

    public static DefaultTableModel modeloTabela(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }

    public static void tabela(JTable t) {
        t.setRowHeight(26);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setBackground(ROXO);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(t.getTableHeader().getFont().deriveFont(Font.BOLD));
        t.getColumnModel().getColumn(0).setMaxWidth(60);
    }

    public static void sucesso(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void erro(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean confirmar(Component pai, String msg) {
        Object[] opcoes = {"Sim", "Não"};
        return JOptionPane.showOptionDialog(pai, msg, "Confirmar", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]) == 0;
    }
}
