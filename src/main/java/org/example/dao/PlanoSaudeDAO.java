package org.example.dao;

import org.example.ConexaoBD;
import org.example.model.PlanoSaude;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanoSaudeDAO {

    public void criarPlano(PlanoSaude plano) {
        String sql = "INSERT INTO RHSTU_PLANO_SAUDE (ID_PLANO, NOME_FANTASIA, RAZAO_SOCIAL, " +
                "STATUS, CNPJ) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plano.getIdPlano());
            stmt.setString(2, plano.getNomeFantasia());
            stmt.setString(3, plano.getRazaoSocial());
            stmt.setString(4, plano.getStatus());
            stmt.setInt(5, plano.getCnpj());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar plano: " + e.getMessage(), e);
        }
    }

    public PlanoSaude buscarPlanoPorId(String idPlano) {
        String sql = "SELECT ID_PLANO, NOME_FANTASIA, RAZAO_SOCIAL, STATUS, CNPJ " +
                "FROM RHSTU_PLANO_SAUDE WHERE ID_PLANO = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPlano);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaPlano(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar plano: " + e.getMessage(), e);
        }
        return null;
    }

    public List<PlanoSaude> listarTodosPLanos() {
        List<PlanoSaude> planos = new ArrayList<>();
        String sql = "SELECT ID_PLANO, NOME_FANTASIA, RAZAO_SOCIAL, STATUS, CNPJ " +
                "FROM RHSTU_PLANO_SAUDE";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                planos.add(mapearResultSetParaPlano(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar planos: " + e.getMessage(), e);
        }
        return planos;
    }

    private PlanoSaude mapearResultSetParaPlano(ResultSet rs) throws SQLException {
        return new PlanoSaude(
                rs.getString("ID_PLANO"),
                rs.getString("NOME_FANTASIA"),
                rs.getString("RAZAO_SOCIAL"),
                rs.getString("STATUS"),
                rs.getInt("CNPJ")
        );
    }
}
