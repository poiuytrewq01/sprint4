package org.example.dao;

import org.example.ConexaoBD;
import org.example.model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    public void criarEndereco(Endereco endereco) {
        String sql = "INSERT INTO RHSTU_ENDERECO (ID_PACIENTE, ID_ENDERECO, NOME_RUA, " +
                "NUMERO, PONTO_REF, CEP, PAIS, ESTADO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco.getIdPaciente());
            stmt.setString(2, endereco.getIdEndereco());
            stmt.setString(3, endereco.getNomeRua());
            stmt.setInt(4, endereco.getNumero());
            stmt.setString(5, endereco.getPontoRef());
            stmt.setString(6, endereco.getCep());
            stmt.setString(7, endereco.getPais());
            stmt.setString(8, endereco.getEstado());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar endereço: " + e.getMessage(), e);
        }
    }

    public List<Endereco> listarEnderecosPorPaciente(String idPaciente) {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT ID_PACIENTE, ID_ENDERECO, NOME_RUA, NUMERO, PONTO_REF, " +
                "CEP, PAIS, ESTADO FROM RHSTU_ENDERECO WHERE ID_PACIENTE = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(mapearResultSetParaEndereco(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar endereços: " + e.getMessage(), e);
        }
        return enderecos;
    }

    private Endereco mapearResultSetParaEndereco(ResultSet rs) throws SQLException {
        return new Endereco(
                rs.getString("ID_PACIENTE"),
                rs.getString("ID_ENDERECO"),
                rs.getString("NOME_RUA"),
                rs.getInt("NUMERO"),
                rs.getString("PONTO_REF"),
                rs.getString("CEP"),
                rs.getString("PAIS"),
                rs.getString("ESTADO")
        );
    }
}
