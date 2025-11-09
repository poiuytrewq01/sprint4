package org.example.service;

import org.example.dao.*;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SistemaClinicaService implements OperacoesSistema {

    private static final Logger logger = LoggerFactory.getLogger(SistemaClinicaService.class);

    // DAOs para acesso aos dados
    private final PacienteDAO pacienteDAO;
    private final ConsultaDAO consultaDAO;
    private final ContatoDAO contatoDAO;
    private final EnderecoDAO enderecoDAO;
    private final PlanoSaudeDAO planoSaudeDAO;

    // Constantes de validação
    private static final int TELEFONE_MIN_DIGITOS = 10;
    private static final int TELEFONE_MAX_DIGITOS = 11;
    private static final int IDADE_MAIORIDADE = 18;
    private static final int DIAS_ANTECEDENCIA_CANCELAMENTO = 2;
    private static final String PADRAO_TELEFONE = "\\d{10,11}";

    /**
     * Construtor que inicializa todos os DAOs
     */
    public SistemaClinicaService() {
        logger.info("Inicializando SistemaClinicaService");
        this.pacienteDAO = new PacienteDAO();
        this.consultaDAO = new ConsultaDAO();
        this.contatoDAO = new ContatoDAO();
        this.enderecoDAO = new EnderecoDAO();
        this.planoSaudeDAO = new PlanoSaudeDAO();
    }

    // =====================================================
    // OPERAÇÕES DE PACIENTE
    // =====================================================

    @Override
    public void cadastrarPaciente(Paciente paciente) {
        try {
            logger.info("Iniciando cadastro do paciente: {}", paciente.getNome());

            // Validações
            validarPaciente(paciente);

            // Verificar se CPF já existe
            if (pacienteDAO.buscarPorCPF(paciente.getCpf()) != null) {
                String erro = "CPF já cadastrado no sistema";
                logger.warn(erro);
                throw new IllegalArgumentException(erro);
            }

            // Criar paciente
            pacienteDAO.criarPaciente(paciente);
            logger.info("Paciente {} cadastrado com sucesso. ID: {}",
                    paciente.getNome(), paciente.getIdPaciente());

        } catch (IllegalArgumentException e) {
            logger.error("Erro na validação do paciente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar paciente: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao cadastrar paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paciente> listarPacientes() {
        try {
            logger.info("Listando todos os pacientes");
            List<Paciente> pacientes = pacienteDAO.listarTodosPacientes();
            logger.info("Total de pacientes retornados: {}", pacientes.size());
            return pacientes;
        } catch (Exception e) {
            logger.error("Erro ao listar pacientes: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar pacientes", e);
        }
    }

    @Override
    public Paciente buscarPaciente(int id) {
        try {
            logger.info("Buscando paciente com ID: {}", id);

            if (id <= 0) {
                throw new IllegalArgumentException("ID do paciente inválido");
            }

            Paciente paciente = pacienteDAO.buscarPacientePorId(String.valueOf(id));

            if (paciente == null) {
                logger.warn("Paciente não encontrado com ID: {}", id);
                throw new IllegalArgumentException("Paciente não encontrado");
            }

            logger.info("Paciente encontrado: {}", paciente.getNome());
            return paciente;

        } catch (IllegalArgumentException e) {
            logger.error("Erro na busca do paciente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao buscar paciente: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar paciente", e);
        }
    }

    @Override
    public void atualizarPaciente(Paciente paciente) {
        try {
            logger.info("Atualizando paciente: {}", paciente.getIdPaciente());

            // Validações
            validarPaciente(paciente);

            // Verificar se paciente existe
            if (pacienteDAO.buscarPacientePorId(paciente.getIdPaciente()) == null) {
                throw new IllegalArgumentException("Paciente não encontrado para atualização");
            }

            pacienteDAO.atualizarPaciente(paciente);
            logger.info("Paciente {} atualizado com sucesso", paciente.getIdPaciente());

        } catch (IllegalArgumentException e) {
            logger.error("Erro na validação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao atualizar paciente: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar paciente", e);
        }
    }

    @Override
    public void removerPaciente(int id) {
        try {
            logger.info("Removendo paciente com ID: {}", id);

            if (id <= 0) {
                throw new IllegalArgumentException("ID do paciente inválido");
            }

            // Verificar se paciente existe
            if (pacienteDAO.buscarPacientePorId(String.valueOf(id)) == null) {
                throw new IllegalArgumentException("Paciente não encontrado");
            }

            pacienteDAO.deletarPaciente(String.valueOf(id));
            logger.info("Paciente {} removido com sucesso", id);

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao remover paciente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover paciente: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao remover paciente", e);
        }
    }


    public Paciente buscarPacientePorCPF(String cpf) {
        try {
            if (cpf == null || cpf.trim().isEmpty()) {
                throw new IllegalArgumentException("CPF não pode estar vazio");
            }

            logger.info("Buscando paciente com CPF: {}", cpf);
            Paciente paciente = pacienteDAO.buscarPorCPF(cpf);

            if (paciente == null) {
                throw new IllegalArgumentException("Paciente com CPF não encontrado");
            }

            logger.info("Paciente encontrado por CPF");
            return paciente;

        } catch (IllegalArgumentException e) {
            logger.error("Erro na busca por CPF: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao buscar por CPF: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar por CPF", e);
        }
    }


    @Override
    public void agendarConsulta(Consulta consulta) {
        try {
            logger.info("Agendando consulta para paciente: {}", consulta.getPacienteId());

            // Validações
            validarConsulta(consulta);

            // Verificar se data é futura
            if (!consulta.dataFutura()) {
                throw new IllegalArgumentException("Data da consulta deve ser futura");
            }

            // Verificar se paciente existe
            if (pacienteDAO.buscarPacientePorId(String.valueOf(consulta.getPacienteId())) == null) {
                throw new IllegalArgumentException("Paciente não encontrado");
            }

            consultaDAO.agendarConsulta(consulta);
            logger.info("Consulta agendada com sucesso para o paciente: {}", consulta.getPacienteId());

        } catch (IllegalArgumentException e) {
            logger.error("Erro na validação da consulta: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao agendar consulta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao agendar consulta", e);
        }
    }

    @Override
    public List<Consulta> listarConsultas() {
        try {
            logger.info("Listando todas as consultas");
            List<Consulta> consultas = consultaDAO.listarConsultas();
            logger.info("Total de consultas retornadas: {}", consultas.size());
            return consultas;
        } catch (Exception e) {
            logger.error("Erro ao listar consultas: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar consultas", e);
        }
    }

    @Override
    public void cancelarConsulta(int id) {
        try {
            logger.info("Cancelando consulta com ID: {}", id);

            Consulta consulta = consultaDAO.buscarConsultaPorId(id);

            if (consulta == null) {
                throw new IllegalArgumentException("Consulta não encontrada");
            }

            if (!consulta.podeCancelar()) {
                throw new IllegalArgumentException(
                        "Consulta não pode ser cancelada. Verifique o status e a data.");
            }

            consultaDAO.cancelarConsulta(id);
            logger.info("Consulta {} cancelada com sucesso", id);

        } catch (IllegalArgumentException e) {
            logger.error("Erro no cancelamento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cancelar consulta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao cancelar consulta", e);
        }
    }

    public void remarcarConsulta(int consultaId, String novaData) {
        try {
            logger.info("Remarcando consulta {} para data: {}", consultaId, novaData);

            Consulta consulta = consultaDAO.buscarConsultaPorId(consultaId);

            if (consulta == null) {
                throw new IllegalArgumentException("Consulta não encontrada");
            }

            if (!consulta.podeRemarcar()) {
                throw new IllegalArgumentException("Consulta não pode ser remarcada neste status");
            }

            consulta.setDataPrevista(novaData);
            consultaDAO.atualizarConsulta(consulta);
            logger.info("Consulta {} remarcada com sucesso", consultaId);

        } catch (IllegalArgumentException e) {
            logger.error("Erro na remarcação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remarcar consulta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao remarcar consulta", e);
        }
    }

    public List<Consulta> listarConsultasPaciente(int pacienteId) {
        try {
            if (pacienteId <= 0) {
                throw new IllegalArgumentException("ID do paciente inválido");
            }

            logger.info("Listando consultas do paciente: {}", pacienteId);
            List<Consulta> consultas = consultaDAO.listarConsultasPorPaciente(pacienteId);
            logger.info("Total de consultas do paciente: {}", consultas.size());
            return consultas;

        } catch (IllegalArgumentException e) {
            logger.error("Erro na listagem: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar consultas do paciente: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar consultas", e);
        }
    }

    public List<Consulta> listarConsultasPorPeriodo(String dataInicio, String dataFim) {
        try {
            if (dataInicio == null || dataFim == null) {
                throw new IllegalArgumentException("Datas não podem estar vazias");
            }

            logger.info("Listando consultas do período: {} até {}", dataInicio, dataFim);
            List<Consulta> consultas = consultaDAO.listarConsultasPorPeriodo(dataInicio, dataFim);
            logger.info("Total de consultas no período: {}", consultas.size());
            return consultas;

        } catch (IllegalArgumentException e) {
            logger.error("Erro no filtro de período: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar por período: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar consultas por período", e);
        }
    }

    // =====================================================
    // RELATÓRIOS E ESTATÍSTICAS
    // =====================================================

    @Override
    public String verificarDisponibilidadePaciente(int pacienteId, String data) {
        try {
            logger.info("Verificando disponibilidade do paciente {} para data {}", pacienteId, data);

            Paciente paciente = pacienteDAO.buscarPacientePorId(String.valueOf(pacienteId));
            if (paciente == null) {
                return "Paciente não encontrado";
            }


            List<Consulta> consultas = consultaDAO.listarConsultasPorPaciente(pacienteId);
            for (Consulta consulta : consultas) {
                if (consulta.getDataPrevista().equals(data) &&
                        ("agendada".equalsIgnoreCase(consulta.getStatus()) ||
                                "confirmada".equalsIgnoreCase(consulta.getStatus()))) {
                    return "Paciente já possui consulta marcada nesta data";
                }
            }

            return "Paciente disponível para agendamento";

        } catch (Exception e) {
            logger.error("Erro ao verificar disponibilidade: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao verificar disponibilidade", e);
        }
    }

    @Override
    public String gerarRelatorioConsultas() {
        try {
            logger.info("Gerando relatório de consultas");

            List<Consulta> consultas = consultaDAO.listarConsultas();

            // Contar por status usando streams
            Map<String, Long> estatisticas = consultas.stream()
                    .collect(Collectors.groupingBy(
                            c -> c.getStatus() != null ? c.getStatus() : "desconhecido",
                            Collectors.counting()
                    ));

            long agendadas = estatisticas.getOrDefault("agendada", 0L);
            long confirmadas = estatisticas.getOrDefault("confirmada", 0L);
            long concluidas = estatisticas.getOrDefault("concluida", 0L);
            long canceladas = estatisticas.getOrDefault("cancelada", 0L);

            String relatorio = String.format("""
                    ╔════════════════════════════════════════╗
                    ║      RELATÓRIO DE CONSULTAS            ║
                    ╠════════════════════════════════════════╣
                    ║ Total de consultas:         %3d        ║
                    ║ Consultas agendadas:        %3d        ║
                    ║ Consultas confirmadas:      %3d        ║
                    ║ Consultas concluídas:       %3d        ║
                    ║ Consultas canceladas:       %3d        ║
                    ╚════════════════════════════════════════╝
                    """,
                    consultas.size(), agendadas, confirmadas, concluidas, canceladas);

            logger.info("Relatório gerado com sucesso");
            return relatorio;

        } catch (Exception e) {
            logger.error("Erro ao gerar relatório: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }




    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não pode ser nulo");
        }

        if (paciente.getNome() == null || paciente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do paciente é obrigatório");
        }

        if (paciente.getCpf() == null || paciente.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }

        if (!validarFormatoCPF(paciente.getCpf())) {
            throw new IllegalArgumentException("Formato de CPF inválido");
        }


        if (paciente.getIdade() < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
    }

    /**
     * Valida todos os dados obrigatórios de uma consulta
     */
    private void validarConsulta(Consulta consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("Consulta não pode ser nula");
        }

        if (!consulta.validarConsulta()) {
            throw new IllegalArgumentException("Dados da consulta inválidos");
        }
    }

    /**
     * Valida o formato do CPF (validação simples)
     */
    private boolean validarFormatoCPF(String cpf) {
        if (cpf == null) return false;
        return cpf.matches("\\d{11}");
    }

    /**
     * Valida o formato do telefone
     */
    private boolean validarFormatoTelefone(String telefone) {
        if (telefone == null) return false;
        return telefone.matches(PADRAO_TELEFONE);
    }
}
