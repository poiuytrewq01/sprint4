package org.example.dao;

import org.example.ConexaoBD;
import org.example.model.Contato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {

    public void criarContato(Contato contato) {
        String sql = "INSERT INTO RHSTU_CONTATO (ID_CONTATO, ID_PACIENTE, NOME, ENDERECO, " +
                "NUMERO, TIPO_CTT, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contato.getIdContato());
            stmt.setString(2, contato.getIdPaciente());
            stmt.setString(3, contato.getNome());
            stmt.setString(4, contato.getEndereco());
            stmt.setInt(5, contato.getNumero());
            stmt.setString(6, contato.getTipoCTT());
            stmt.setString(7, contato.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar contato: " + e.getMessage(), e);
        }
    }

    public List<Contato> listarContatosPorPaciente(String idPaciente) {
        List<Contato> contatos = new ArrayList<>();
        String sql = "SELECT ID_CONTATO, ID_PACIENTE, NOME, ENDERECO, NUMERO, TIPO_CTT, STATUS " +
                "FROM RHSTU_CONTATO WHERE ID_PACIENTE = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contatos.add(mapearResultSetParaContato(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contatos: " + e.getMessage(), e);
        }
        return contatos;
    }

    private Contato mapearResultSetParaContato(ResultSet rs) throws SQLException {
        return new Contato(
                rs.getString("ID_CONTATO"),
                rs.getString("ID_PACIENTE"),
                rs.getString("NOME"),
                rs.getString("ENDERECO"),
                rs.getInt("NUMERO"),
                rs.getString("TIPO_CTT"),
                rs.getString("STATUS")
        );
    }
}
