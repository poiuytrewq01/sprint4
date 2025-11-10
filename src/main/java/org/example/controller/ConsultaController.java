package org.example.controller;

import org.example.model.Consulta;
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
 * Controller REST para gerenciar operações com Consultas.
 * Fornece endpoints para agendamento, cancelamento e gerenciamento de consultas.
 *
 * Base URL: /api/v1/consultas
 *
 * @author ConectaHC Team
 * @version 2.0.0
 */
@RestController
@RequestMapping("/api/v1/consultas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ConsultaController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);

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
    // AGENDAMENTO
    // ========================================

    /**
     * POST /api/v1/consultas
     * Agenda uma nova consulta
     *
     * @param consulta Dados da consulta
     * @return ResponseEntity com dados da consulta agendada
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> agendarConsulta(@RequestBody Consulta consulta) {
        try {
            logger.info("Recebendo requisição para agendar consulta para paciente: {}",
                    consulta.getPacienteId());

            if (consulta == null || consulta.getPacienteId() <= 0) {
                logger.warn("Dados de consulta inválidos");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(construirRespostaErro("Dados de consulta inválidos", 400));
            }

            sistemaClinicaService.agendarConsulta(consulta);

            logger.info("Consulta agendada com sucesso para paciente: {}", consulta.getPacienteId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(construirRespostaSucesso(
                            "Consulta agendada com sucesso",
                            consulta,
                            201
                    ));

        } catch (IllegalArgumentException e) {
            logger.warn("Validação falhou ao agendar: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro ao agendar consulta: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao agendar consulta",
                            500
                    ));
        }
    }

    // ========================================
    // LISTAGEM
    // ========================================

    /**
     * GET /api/v1/consultas
     * Lista todas as consultas
     *
     * @return ResponseEntity com lista de consultas
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarConsultas() {
        try {
            logger.info("Listando todas as consultas");

            List<Consulta> consultas = sistemaClinicaService.listarConsultas();

            logger.info("Total de consultas: {}", consultas.size());

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Consultas listadas com sucesso",
                    Map.of(
                            "total", consultas.size(),
                            "consultas", consultas
                    ),
                    200
            ));

        } catch (Exception e) {
            logger.error("Erro ao listar consultas: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao listar consultas",
                            500
                    ));
        }
    }

    /**
     * GET /api/v1/consultas/paciente/{pacienteId}
     * Lista consultas de um paciente específico
     *
     * @param pacienteId ID do paciente
     * @return ResponseEntity com lista de consultas
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Map<String, Object>> listarConsultasPaciente(@PathVariable int pacienteId) {
        try {
            logger.info("Listando consultas do paciente: {}", pacienteId);

            List<Consulta> consultas = sistemaClinicaService.listarConsultasPaciente(pacienteId);

            logger.info("Total de consultas do paciente: {}", consultas.size());

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Consultas do paciente listadas com sucesso",
                    Map.of(
                            "pacienteId", pacienteId,
                            "total", consultas.size(),
                            "consultas", consultas
                    ),
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Erro ao listar consultas: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro ao listar consultas do paciente: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao listar consultas",
                            500
                    ));
        }
    }

    /**
     * GET /api/v1/consultas/periodo?dataInicio=...&dataFim=...
     * Lista consultas em um período específico
     *
     * @param dataInicio Data de início (YYYY-MM-DD)
     * @param dataFim Data de fim (YYYY-MM-DD)
     * @return ResponseEntity com lista de consultas
     */
    @GetMapping("/periodo")
    public ResponseEntity<Map<String, Object>> listarConsultasPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        try {
            logger.info("Listando consultas do período: {} até {}", dataInicio, dataFim);

            List<Consulta> consultas = sistemaClinicaService.listarConsultasPorPeriodo(
                    dataInicio, dataFim);

            logger.info("Total de consultas no período: {}", consultas.size());

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Consultas do período listadas com sucesso",
                    Map.of(
                            "dataInicio", dataInicio,
                            "dataFim", dataFim,
                            "total", consultas.size(),
                            "consultas", consultas
                    ),
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Erro ao filtrar por período: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro ao listar por período: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao listar por período",
                            500
                    ));
        }
    }

    // ========================================
    // OPERAÇÕES ESPECIAIS
    // ========================================

    /**
     * PUT /api/v1/consultas/{id}/remarcar?novaData=...
     * Remarca uma consulta para uma nova data
     *
     * @param id ID da consulta
     * @param novaData Nova data (YYYY-MM-DD)
     * @return ResponseEntity com confirmação
     */
    @PutMapping("/{id}/remarcar")
    public ResponseEntity<Map<String, Object>> remarcarConsulta(
            @PathVariable int id,
            @RequestParam String novaData) {
        try {
            logger.info("Remarcando consulta {} para {}", id, novaData);

            if (novaData == null || novaData.isEmpty()) {
                logger.warn("Nova data inválida");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(construirRespostaErro("Nova data é obrigatória", 400));
            }

            sistemaClinicaService.remarcarConsulta(id, novaData);

            logger.info("Consulta {} remarcada com sucesso", id);

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Consulta remarcada com sucesso",
                    Map.of(
                            "consultaId", id,
                            "novaData", novaData
                    ),
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Erro ao remarcar: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro ao remarcar consulta: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao remarcar consulta",
                            500
                    ));
        }
    }

    /**
     * GET /api/v1/consultas/disponibilidade?pacienteId=...&data=...
     * Verifica disponibilidade de um paciente
     *
     * @param pacienteId ID do paciente
     * @param data Data a verificar (YYYY-MM-DD)
     * @return ResponseEntity com resultado da verificação
     */
    @GetMapping("/disponibilidade")
    public ResponseEntity<Map<String, Object>> verificarDisponibilidade(
            @RequestParam int pacienteId,
            @RequestParam String data) {
        try {
            logger.info("Verificando disponibilidade do paciente {} para {}", pacienteId, data);

            String resultado = sistemaClinicaService.verificarDisponibilidadePaciente(
                    pacienteId, data);

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Disponibilidade verificada",
                    Map.of(
                            "pacienteId", pacienteId,
                            "data", data,
                            "disponivel", resultado.contains("disponível"),
                            "resultado", resultado
                    ),
                    200
            ));

        } catch (Exception e) {
            logger.error("Erro ao verificar disponibilidade: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao verificar disponibilidade",
                            500
                    ));
        }
    }

    /**
     * DELETE /api/v1/consultas/{id}
     * Cancela uma consulta
     *
     * @param id ID da consulta
     * @return ResponseEntity com confirmação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelarConsulta(@PathVariable int id) {
        try {
            logger.info("Cancelando consulta com ID: {}", id);

            sistemaClinicaService.cancelarConsulta(id);

            logger.info("Consulta {} cancelada com sucesso", id);

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Consulta cancelada com sucesso",
                    Map.of("consultaId", id),
                    200
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Erro ao cancelar: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(construirRespostaErro(e.getMessage(), 400));

        } catch (Exception e) {
            logger.error("Erro ao cancelar consulta: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao cancelar consulta",
                            500
                    ));
        }
    }

    // ========================================
    // RELATÓRIOS
    // ========================================

    /**
     * GET /api/v1/consultas/relatorio
     * Gera relatório de consultas
     *
     * @return ResponseEntity com relatório
     */
    @GetMapping("/relatorio")
    public ResponseEntity<Map<String, Object>> gerarRelatorio() {
        try {
            logger.info("Gerando relatório de consultas");

            String relatorio = sistemaClinicaService.gerarRelatorioConsultas();

            return ResponseEntity.ok(construirRespostaSucesso(
                    "Relatório gerado com sucesso",
                    Map.of("relatorio", relatorio),
                    200
            ));

        } catch (Exception e) {
            logger.error("Erro ao gerar relatório: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(construirRespostaErro(
                            "Erro ao gerar relatório",
                            500
                    ));
        }
    }

    // ========================================
    // HEALTH CHECK
    // ========================================

    /**
     * GET /api/v1/consultas/health
     * Verifica se o controlador está operacional
     *
     * @return ResponseEntity com status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check do ConsultaController");
        return ResponseEntity.ok(construirRespostaSucesso(
                "ConsultaController está operacional",
                Map.of(
                        "servico", "ConsultaController",
                        "versao", "2.0.0",
                        "status", "ativo"
                ),
                200
        ));
    }
}
