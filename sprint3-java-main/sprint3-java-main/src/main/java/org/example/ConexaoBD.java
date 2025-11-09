package org.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "RM565597";
    private static final String PASSWORD = "241105";


    static {
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver Oracle JDBC não encontrado!", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o Oracle: " + e.getMessage(), e);
        }
    }

    public static void testarConexao() {
        try (Connection conn = getConnection()) {
            System.out.println("Conexão com Oracle estabelecida com sucesso!");
            System.out.println("Banco: Oracle " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com Oracle: " + e.getMessage());
            System.out.println("Verifique:");
            System.out.println("   - Se o Oracle Database está rodando");
            System.out.println("   - Se o Listener está ativo (porta 1521)");
            System.out.println("   - Usuário e senha corretos");
            System.out.println("   - Service Name/SID correto");
        }
    }
}
