package br.com.neuraxi.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

// Validações reaproveitadas pelos controllers
public class Validador {

    private static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public static void obrigatorio(String valor, String campo) throws RegraNegocioException {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException("O campo " + campo + " é obrigatório.");
        }
    }

    public static LocalDate data(String valor, String campo) throws RegraNegocioException {
        try {
            return LocalDate.parse(valor.trim(), DATA);
        } catch (DateTimeParseException e) {
            throw new RegraNegocioException(campo + " inválida. Use dd/mm/aaaa.");
        }
    }

    public static LocalDateTime dataHora(String valor, String campo) throws RegraNegocioException {
        try {
            return LocalDateTime.parse(valor.trim(), DATA_HORA);
        } catch (DateTimeParseException e) {
            throw new RegraNegocioException(campo + " inválida. Use dd/mm/aaaa hh:mm.");
        }
    }

    public static String telefone(String valor) throws RegraNegocioException {
        String numeros = valor.replaceAll("\\D", "");
        if (numeros.length() < 10 || numeros.length() > 11) {
            throw new RegraNegocioException("Telefone inválido. Informe DDD + número (10 ou 11 dígitos).");
        }
        return numeros;
    }
}
