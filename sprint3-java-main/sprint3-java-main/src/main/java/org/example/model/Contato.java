package org.example.model;

public class Contato {

    private String idContato;
    private String idPaciente;
    private String nome;
    private String endereco;
    private Integer numero;
    private String tipoCTT;
    private String status;

    // Construtores
    public Contato() {}

    public Contato(String idContato, String idPaciente, String nome, String endereco,
                   Integer numero, String tipoCTT, String status) {
        this.idContato = idContato;
        this.idPaciente = idPaciente;
        this.nome = nome;
        this.endereco = endereco;
        this.numero = numero;
        this.tipoCTT = tipoCTT;
        this.status = status;
    }

    // Getters e Setters
    public String getIdContato() { return idContato; }
    public void setIdContato(String idContato) { this.idContato = idContato; }

    public String getIdPaciente() { return idPaciente; }
    public void setIdPaciente(String idPaciente) { this.idPaciente = idPaciente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getTipoCTT() { return tipoCTT; }
    public void setTipoCTT(String tipoCTT) { this.tipoCTT = tipoCTT; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Contato{ID: %s, Nome: %s, Tipo: %s}", idContato, nome, tipoCTT);
    }
}
