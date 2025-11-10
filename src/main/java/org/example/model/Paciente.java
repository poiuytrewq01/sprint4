package org.example.model;

import java.time.LocalDate;
import java.util.List;

public class Paciente {

    private String idPaciente;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String tpSanguineo;
    private String altura;
    private String peso;
    private String rg;
    private int idade;
    private String escolaridade;
    private String idEstadoCivil;
    private String idPlano;
    private List<Contato> contatos;
    private List<ContatoEmergencia> contatosEmergencia;
    private List<Endereco> enderecos;;
    private List<PacientePlanoSaude> planosSaude;

    // Construtores
    public Paciente() {}

    public Paciente(String idPaciente, String nome, String cpf, LocalDate dataNascimento) {
        this.idPaciente = idPaciente;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public String getIdPaciente() { return idPaciente; }
    public void setIdPaciente(String idPaciente) { this.idPaciente = idPaciente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTpSanguineo() { return tpSanguineo; }
    public void setTpSanguineo(String tpSanguineo) { this.tpSanguineo = tpSanguineo; }

    public String getAltura() { return altura; }
    public void setAltura(String altura) { this.altura = altura; }

    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getEscolaridade() { return escolaridade; }
    public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }

    public String getIdEstadoCivil() { return idEstadoCivil; }
    public void setIdEstadoCivil(String idEstadoCivil) { this.idEstadoCivil = idEstadoCivil; }

    public String getIdPlano() { return idPlano; }
    public void setIdPlano(String idPlano) { this.idPlano = idPlano; }

    public List<Contato> getContatos() { return contatos; }
    public void setContatos(List<Contato> contatos) { this.contatos = contatos; }

    public List<ContatoEmergencia> getContatosEmergencia() { return contatosEmergencia; }
    public void setContatosEmergencia(List<ContatoEmergencia> contatosEmergencia) {
        this.contatosEmergencia = contatosEmergencia;
    }

    public List<Endereco> getEnderecos() { return enderecos; }
    public void setEnderecos(List<Endereco> enderecos) { this.enderecos = enderecos; }


    public List<PacientePlanoSaude> getPlanosSaude() { return planosSaude; }
    public void setPlanosSaude(List<PacientePlanoSaude> planosSaude) { this.planosSaude = planosSaude; }

    @Override
    public String toString() {
        return String.format("Paciente{ID: %s, Nome: %s, CPF: %s}", idPaciente, nome, cpf);
    }
}
