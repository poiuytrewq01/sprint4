package org.example.dao;

import org.example.ConexaoBD;
import org.example.model.Paciente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public void criarPaciente(Paciente paciente) {
        String sql = "INSERT INTO RHSTU_PACIENTE (ID_PACIENTE, NOME, CPF, DATA_NASCIMENTO, " +
                "TP_SANGUINEO, ALTURA, PESO, RG, IDADE, ESCOLARIDADE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getIdPaciente());
            stmt.setString(2, paciente.getNome());
            stmt.setString(3, paciente.getCpf());
            stmt.setObject(4, paciente.getDataNascimento());
            stmt.setString(5, paciente.getTpSanguineo());
            stmt.setString(6, paciente.getAltura());
            stmt.setString(7, paciente.getPeso());
            stmt.setString(8, paciente.getRg());
            stmt.setInt(9, paciente.getIdade());
            stmt.setString(10, paciente.getEscolaridade());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar paciente: " + e.getMessage(), e);
        }
    }

    public Paciente buscarPacientePorId(String idPaciente) {
        String sql = "SELECT ID_PACIENTE, NOME, CPF, DATA_NASCIMENTO, TP_SANGUINEO, " +
                "ALTURA, PESO, RG, IDADE, ESCOLARIDADE" +
                "FROM RHSTU_PACIENTE WHERE ID_PACIENTE = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaPaciente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar paciente: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Paciente> listarTodosPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT ID_PACIENTE, NOME, CPF, DATA_NASCIMENTO, TP_SANGUINEO, " +
                "ALTURA, PESO, RG, IDADE, ESCOLARIDADE " +
                "FROM RHSTU_PACIENTE ORDER BY NOME";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearResultSetParaPaciente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage(), e);
        }
        return pacientes;
    }

    public Paciente buscarPorCPF(String cpf) {
        String sql = "SELECT ID_PACIENTE, NOME, CPF, DATA_NASCIMENTO, TP_SANGUINEO, " +
                "ALTURA, PESO, RG, IDADE, ESCOLARIDADE " +
                "FROM RHSTU_PACIENTE WHERE CPF = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaPaciente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por CPF: " + e.getMessage(), e);
        }
        return null;
    }

    public void atualizarPaciente(Paciente paciente) {
        String sql = "UPDATE RHSTU_PACIENTE SET NOME = ?, CPF = ?, DATA_NASCIMENTO = ?, " +
                "TP_SANGUINEO = ?, ALTURA = ?, PESO = ?, RG = ?, IDADE = ?, ESCOLARIDADE = ?, " +
                "WHERE ID_PACIENTE = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setObject(3, paciente.getDataNascimento());
            stmt.setString(4, paciente.getTpSanguineo());
            stmt.setString(5, paciente.getAltura());
            stmt.setString(6, paciente.getPeso());
            stmt.setString(7, paciente.getRg());
            stmt.setInt(8, paciente.getIdade());
            stmt.setString(9, paciente.getEscolaridade());
            stmt.setString(12, paciente.getIdPaciente());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar paciente: " + e.getMessage(), e);
        }
    }

    public void deletarPaciente(String idPaciente) {
        String sql = "DELETE FROM RHSTU_PACIENTE WHERE ID_PACIENTE = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPaciente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar paciente: " + e.getMessage(), e);
        }
    }

    private Paciente mapearResultSetParaPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getString("ID_PACIENTE"));
        paciente.setNome(rs.getString("NOME"));
        paciente.setCpf(rs.getString("CPF"));
        paciente.setDataNascimento(rs.getObject("DATA_NASCIMENTO") != null ?
                LocalDate.parse(rs.getObject("DATA_NASCIMENTO").toString()) : null);
        paciente.setTpSanguineo(rs.getString("TP_SANGUINEO"));
        paciente.setAltura(rs.getString("ALTURA"));
        paciente.setPeso(rs.getString("PESO"));
        paciente.setRg(rs.getString("RG"));
        paciente.setIdade(rs.getInt("IDADE"));
        paciente.setEscolaridade(rs.getString("ESCOLARIDADE"));
        return paciente;
    }
}
