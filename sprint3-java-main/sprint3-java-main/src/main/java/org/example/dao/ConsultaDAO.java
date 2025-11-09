package org.example.dao;

import org.example.ConexaoBD;
import org.example.model.Consulta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public void agendarConsulta(Consulta consulta) {
        String sql = "INSERT INTO consultas (paciente_id, data_consulta, status, motivo, observacoes) " +
                "VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {

            stmt.setInt(1, consulta.getPacienteId());
            stmt.setString(2, consulta.getDataPrevista());
            stmt.setString(3, consulta.getStatus());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getObservacoes());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consulta.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao agendar consulta: " + e.getMessage(), e);
        }
    }

    public List<Consulta> listarConsultas() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT id, paciente_id, data_consulta, status, motivo, observacoes " +
                "FROM consultas ORDER BY data_consulta DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                consultas.add(mapearResultSetParaConsulta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas: " + e.getMessage(), e);
        }
        return consultas;
    }

    public List<Consulta> listarConsultasPorPaciente(int pacienteId) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT id, paciente_id, data_consulta, status, motivo, observacoes " +
                "FROM consultas WHERE paciente_id = ? ORDER BY data_consulta DESC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearResultSetParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas do paciente: " + e.getMessage(), e);
        }
        return consultas;
    }

    public Consulta buscarConsultaPorId(int id) {
        String sql = "SELECT id, paciente_id, data_consulta, status, motivo, observacoes " +
                "FROM consultas WHERE id = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaConsulta(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar consulta: " + e.getMessage(), e);
        }
        return null;
    }

    public void atualizarConsulta(Consulta consulta) {
        String sql = "UPDATE consultas SET paciente_id = ?, data_consulta = TO_DATE(?, 'YYYY-MM-DD'), " +
                "status = ?, motivo = ?, observacoes = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getPacienteId());
            stmt.setString(2, consulta.getDataPrevista());
            stmt.setString(3, consulta.getStatus());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getObservacoes());
            stmt.setInt(6, consulta.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar consulta: " + e.getMessage(), e);
        }
    }

    public void cancelarConsulta(int id) {
        String sql = "UPDATE consultas SET status = 'cancelada' WHERE id = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar consulta: " + e.getMessage(), e);
        }
    }

    public void deletarConsulta(int id) {
        String sql = "DELETE FROM consultas WHERE id = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar consulta: " + e.getMessage(), e);
        }
    }

    public List<Consulta> listarConsultasPorPeriodo(String dataInicio, String dataFim) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT id, paciente_id, data_consulta, status, motivo, observacoes " +
                "FROM consultas WHERE data_consulta BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') " +
                "ORDER BY data_consulta ASC";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dataInicio);
            stmt.setString(2, dataFim);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearResultSetParaConsulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas por período: " + e.getMessage(), e);
        }
        return consultas;
    }

    public long contarConsultas() {
        String sql = "SELECT COUNT(*) as total FROM consultas";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar consultas: " + e.getMessage(), e);
        }
        return 0;
    }

    public long contarConsultasPorStatus(String status) {
        String sql = "SELECT COUNT(*) as total FROM consultas WHERE status = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar consultas por status: " + e.getMessage(), e);
        }
        return 0;
    }

    private Consulta mapearResultSetParaConsulta(ResultSet rs) throws SQLException {
        return new Consulta(
                rs.getInt("id"),
                rs.getInt("paciente_id"),
                rs.getString("data_consulta"),
                rs.getString("status"),
                rs.getString("motivo"),
                rs.getString("observacoes")
        );
    }
}
