package br.com.neuraxi.controller;

// Erro de regra de negócio ou de banco, com mensagem pronta para mostrar na tela
public class RegraNegocioException extends Exception {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
