package org.example.model;

import java.time.LocalDate;

public class PacientePlanoSaude {
    private String pacienteIdPaciente;
    private String planoSaudeIdPlano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;

    // Construtores
    public PacientePlanoSaude() {}

    public PacientePlanoSaude(String pacienteIdPaciente, String planoSaudeIdPlano,
                              LocalDate dataInicio, LocalDate dataFim, String descricao) {
        this.pacienteIdPaciente = pacienteIdPaciente;
        this.planoSaudeIdPlano = planoSaudeIdPlano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getPacienteIdPaciente() { return pacienteIdPaciente; }
    public void setPacienteIdPaciente(String pacienteIdPaciente) { this.pacienteIdPaciente = pacienteIdPaciente; }

    public String getPlanoSaudeIdPlano() { return planoSaudeIdPlano; }
    public void setPlanoSaudeIdPlano(String planoSaudeIdPlano) { this.planoSaudeIdPlano = planoSaudeIdPlano; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return String.format("PacientePlanoSaude{Plano: %s, De: %s até %s}",
                planoSaudeIdPlano, dataInicio, dataFim);
    }
}
