package br.com.neuraxi.model;

// Entidade Paciente (tabela paciente)
public class Paciente {
    private int id;
    private String nome;
    private String telefone;
    private String dataNascimento; // dd/mm/aaaa

    public Paciente() {
    }

    public Paciente(int id, String nome, String telefone, String dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    // Usado no combo de pacientes da tela de agendamentos
    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
