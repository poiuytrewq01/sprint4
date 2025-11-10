package org.example.service;

import org.example.model.Consulta;
import org.example.model.Paciente;
import java.util.List;

public interface OperacoesSistema {
    void cadastrarPaciente(Paciente paciente);
    List<Paciente> listarPacientes();
    Paciente buscarPaciente(int id);
    void atualizarPaciente(Paciente paciente);
    void removerPaciente(int id);
    void agendarConsulta(Consulta consulta);
    List<Consulta> listarConsultas();
    void cancelarConsulta(int id);
    String verificarDisponibilidadePaciente(int pacienteId, String data);
    String gerarRelatorioConsultas();
}
