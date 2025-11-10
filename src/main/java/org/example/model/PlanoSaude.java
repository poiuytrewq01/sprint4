package org.example.model;

public class PlanoSaude {

    private String idPlano;
    private String nomeFantasia;
    private String razaoSocial;
    private String status;
    private Integer cnpj;

    // Construtores
    public PlanoSaude() {}

    public PlanoSaude(String idPlano, String nomeFantasia, String razaoSocial,
                      String status, Integer cnpj) {
        this.idPlano = idPlano;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.status = status;
        this.cnpj = cnpj;
    }

    // Getters e Setters
    public String getIdPlano() { return idPlano; }
    public void setIdPlano(String idPlano) { this.idPlano = idPlano; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCnpj() { return cnpj; }
    public void setCnpj(Integer cnpj) { this.cnpj = cnpj; }

    @Override
    public String toString() {
        return String.format("PlanoSaude{ID: %s, Nome: %s}", idPlano, nomeFantasia);
    }
}
