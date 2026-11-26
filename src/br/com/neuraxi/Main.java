package br.com.neuraxi;

import br.com.neuraxi.model.dao.ConexaoBD;
import br.com.neuraxi.view.TelaPrincipal;

import javax.swing.*;

// Ponto de entrada: cria o banco (se não existir) e abre a tela
public class Main {
    public static void main(String[] args) {
        try {
            ConexaoBD.inicializarBanco();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
