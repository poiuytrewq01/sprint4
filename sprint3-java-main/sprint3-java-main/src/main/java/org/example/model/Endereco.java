package org.example.model;

public class Endereco {

    private String idPaciente;
    private String idEndereco;
    private String nomeRua;
    private Integer numero;
    private String pontoRef;
    private String cep;
    private String pais;
    private String estado;

    // Construtores
    public Endereco() {}

    public Endereco(String idPaciente, String idEndereco, String nomeRua,
                    Integer numero, String pontoRef, String cep, String pais, String estado) {
        this.idPaciente = idPaciente;
        this.idEndereco = idEndereco;
        this.nomeRua = nomeRua;
        this.numero = numero;
        this.pontoRef = pontoRef;
        this.cep = cep;
        this.pais = pais;
        this.estado = estado;
    }

    // Getters e Setters
    public String getIdPaciente() { return idPaciente; }
    public void setIdPaciente(String idPaciente) { this.idPaciente = idPaciente; }

    public String getIdEndereco() { return idEndereco; }
    public void setIdEndereco(String idEndereco) { this.idEndereco = idEndereco; }

    public String getNomeRua() { return nomeRua; }
    public void setNomeRua(String nomeRua) { this.nomeRua = nomeRua; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getPontoRef() { return pontoRef; }
    public void setPontoRef(String pontoRef) { this.pontoRef = pontoRef; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("Endereco{%s, %d, %s - %s}", nomeRua, numero, cep, estado);
    }
}
