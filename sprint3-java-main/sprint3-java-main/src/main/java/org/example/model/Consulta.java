package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Consulta {

    private int id;
    private int pacienteId;
    private String dataPrevista;
    private String status;
    private String motivo;
    private String observacoes;

    // Construtores
    public Consulta() {}

    public Consulta(int pacienteId, String dataPrevista) {
        this.pacienteId = pacienteId;
        this.dataPrevista = dataPrevista;
        this.status = "agendada";
    }

    public Consulta(int id, int pacienteId, String dataPrevista, String status) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.dataPrevista = dataPrevista;
        this.status = status;
    }

    public Consulta(int id, int pacienteId, String dataPrevista, String status, String motivo, String observacoes) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.dataPrevista = dataPrevista;
        this.status = status;
        this.motivo = motivo;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }

    public String getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(String dataPrevista) { this.dataPrevista = dataPrevista; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // Métodos de negócio
    public boolean dataFutura() {
        try {
            LocalDate dataConsulta = LocalDate.parse(this.dataPrevista);
            return dataConsulta.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean podeCancelar() {
        try {
            LocalDate dataConsulta = LocalDate.parse(this.dataPrevista);
            LocalDate hoje = LocalDate.now();
            return dataConsulta.isAfter(hoje.plusDays(1)) &&
                    ("agendada".equalsIgnoreCase(this.status) || "confirmada".equalsIgnoreCase(this.status));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean podeRemarcar() {
        return !("concluida".equalsIgnoreCase(this.status) || "cancelada".equalsIgnoreCase(this.status));
    }

    public String formatarData() {
        try {
            LocalDate data = LocalDate.parse(this.dataPrevista);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return data.format(formatter);
        } catch (DateTimeParseException e) {
            return "Data inválida";
        }
    }

    public boolean validarConsulta() {
        return this.pacienteId > 0 &&
                this.dataPrevista != null && !this.dataPrevista.trim().isEmpty() &&
                this.dataFutura() &&
                this.status != null && !this.status.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Paciente ID: %d | Data: %s | Status: %s | Motivo: %s",
                id, pacienteId, formatarData(), status, motivo);
    }
}
