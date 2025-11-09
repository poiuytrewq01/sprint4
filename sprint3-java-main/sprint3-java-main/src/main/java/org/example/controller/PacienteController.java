package org.example.controller;

import org.example.model.Paciente;
import org.example.service.SistemaClinicaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciar operações com Pacientes.
 * Fornece endpoints para CRUD completo de pacientes com validações.
 *
 * Base URL: /api/v1/pacientes
 *
 * @author ConectaHC Team
 * @version 2.0.0
 */
@RestController
@RequestMapping("/api/v1/pacientes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private SistemaClinicaService sistemaClinicaService;

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    /**
     * Constrói uma resposta padronizada de sucesso
     */
    private Map<String, Object> construirRespostaSucesso(String mensagem, Object dados, int status) {
        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", true);
        response.put("mensagem", mensagem);
        response.put("dados", dados);
        response.put("status", status);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Constrói uma resposta padronizada de erro
     */
    private Map<String, Object> construirRespostaErro(String mensagem, int status) {
        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", false);
        response.put("mensagem", mensagem);
        response.put("status", status);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    // ========================================
    // CRUD - CREATE
    // ========================================

    /**
     * POST /api/v1/pacientes
     * Cria um novo paciente
     *
     * @param paciente Dados do paciente a ser criado
     * @return ResponseEntity com dados do paciente criado
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarPaciente(@RequestBody Paciente paciente) {
        try {
            logger.info("Recebendo requisição para criar paciente: {}", paciente.getNome());

            // Validar entrada
            if (paciente == null || paciente.getNome() == null || paciente.getNome().isEmpty()) {
                logger.warn("Dados de paciente inválidos recebidos");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(construirRespostaErro("Nome do paciente é obrigatório", 400));
            }

            // Criar paciente
            sistemaClinicaService.cadastrarPaciente(paciente);

            logger.info("Paciente {} criado com sucesso. ID: {}",
                    paciente.getNome(), paciente.getIdPaciente());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(construirRespostaSucesso(
                            "Paciente criado com sucesso",
                            paciente,
                            201
                    ));

        } catch (IllegalArgumentException e) {
            logger.warn("Validação falhou ao criar paciente: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro inesperado ao criar paciente: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao criar paciente: " + e.getMessage(),
                            500
                    ));
        }
    }

    // ========================================
    // CRUD - READ
    // ========================================

    /**
     * GET /api/v1/pacientes
     * Lista todos os pacientes cadastrados
     *
     * @return ResponseEntity com lista de pacientes
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarPacientes() {
        try {
            logger.info("Buscando lista de pacientes");

            List<Paciente> pacientes = sistemaClinicaService.listarPacientes();

            logger.info("Total de pacientes encontrados: {}", pacientes.size());

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Pacientes listados com sucesso",
                    Map.of(
                            "total", pacientes.size(),
                            "pacientes", pacientes
                    ),
                    200
            ));

        } catch (Exception e) {
            logger.error("Erro ao listar pacientes: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao listar pacientes",
                            500
                    ));
        }
    }

    /**
     * GET /api/v1/pacientes/{id}
     * Busca um paciente específico por ID
     *
     * @param id ID do paciente
     * @return ResponseEntity com dados do paciente
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPaciente(@PathVariable int id) {
        try {
            logger.info("Buscando paciente com ID: {}", id);

            Paciente paciente = sistemaClinicaService.buscarPaciente(id);

            logger.info("Paciente encontrado: {}", paciente.getNome());

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Paciente encontrado",
                    paciente,
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Paciente não encontrado. ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(construirRespostaErro(e.getMessage(), 404));

        } catch (Exception e) {
            logger.error("Erro ao buscar paciente: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao buscar paciente",
                            500
                    ));
        }
    }

    /**
     * GET /api/v1/pacientes/buscar/cpf?cpf=...
     * Busca um paciente pelo CPF
     *
     * @param cpf CPF do paciente
     * @return ResponseEntity com dados do paciente
     */
    @GetMapping("/buscar/cpf")
    public ResponseEntity<Map<String, Object>> buscarPorCPF(@RequestParam String cpf) {
        try {
            logger.info("Buscando paciente por CPF: {}", cpf);

            Paciente paciente = sistemaClinicaService.buscarPacientePorCPF(cpf);

            logger.info("Paciente encontrado por CPF");

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Paciente encontrado",
                    paciente,
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Paciente não encontrado. CPF: {}", cpf);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(construirRespostaErro(e.getMessage(), 404));

        } catch (Exception e) {
            logger.error("Erro ao buscar por CPF: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao buscar por CPF",
                            500
                    ));
        }
    }



    // ========================================
    // CRUD - UPDATE
    // ========================================

    /**
     * PUT /api/v1/pacientes/{id}
     * Atualiza os dados de um paciente
     *
     * @param id ID do paciente
     * @param paciente Novos dados do paciente
     * @return ResponseEntity com dados atualizados
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarPaciente(
            @PathVariable int id,
            @RequestBody Paciente paciente) {
        try {
            logger.info("Atualizando paciente com ID: {}", id);

            // Validar entrada
            if (paciente == null) {
                logger.warn("Dados de paciente inválidos");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(construirRespostaErro("Dados do paciente inválidos", 400));
            }

            // Definir ID
            paciente.setIdPaciente(String.valueOf(id));

            // Atualizar
            sistemaClinicaService.atualizarPaciente(paciente);

            logger.info("Paciente {} atualizado com sucesso", id);

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Paciente atualizado com sucesso",
                    paciente,
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Validação falhou ao atualizar: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro ao atualizar paciente: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao atualizar paciente",
                            500
                    ));
        }
    }

    // ========================================
    // CRUD - DELETE
    // ========================================

    /**
     * DELETE /api/v1/pacientes/{id}
     * Deleta um paciente
     *
     * @param id ID do paciente
     * @return ResponseEntity com confirmação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarPaciente(@PathVariable int id) {
        try {
            logger.info("Deletando paciente com ID: {}", id);

            sistemaClinicaService.removerPaciente(id);

            logger.info("Paciente {} deletado com sucesso", id);

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Paciente deletado com sucesso",
                    Map.of("id", id),
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Erro ao deletar paciente: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(construirRespostaErro(e.getMessage(), 404));

        } catch (Exception e) {
            logger.error("Erro ao deletar paciente: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao deletar paciente",
                            500
                    ));
        }
    }

    // ========================================
    // ENDPOINT DE HEALTH CHECK
    // ========================================

    /**
     * GET /api/v1/pacientes/health
     * Verifica se o controlador está operacional
     *
     * @return ResponseEntity com status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check do PacienteController");
        return ResponseEntity.ok(construirRespostaSucesso(
                "PacienteController está operacional",
                Map.of(
                        "servico", "PacienteController",
                        "versao", "2.0.0",
                        "status", "ativo"
                ),
                200
        ));
    }
}
