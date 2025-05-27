import model.*;
import util.*;
import java.util.*;

public class SistemaAcademico {
    private List<Aluno> alunos;
    private List<Professor> professores;
    private List<Disciplina> disciplinas;
    private List<Turma> turmas;
    private List<AvaliacaoFrequencia> avaliacoes;
    private Scanner scanner;
    
    public SistemaAcademico() {
        this.alunos = new ArrayList<>();
        this.professores = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
        this.turmas = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        
        carregarDados();
    }
    
    public void executar() {
        System.out.println("=== SISTEMA ACADÊMICO FCTE ===");
        
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir quebra de linha
            
            switch (opcao) {
                case 1:
                    modoAluno();
                    break;
                case 2:
                    modoDisciplinaTurma();
                    break;
                case 3:
                    modoAvaliacaoFrequencia();
                    break;
                case 4:
                    salvarDados();
                    System.out.println("Dados salvos com sucesso!");
                    break;
                case 0:
                    salvarDados();
                    System.out.println("Sistema encerrado. Dados salvos!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    private void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Modo Aluno");
        System.out.println("2. Modo Disciplina/Turma");
        System.out.println("3. Modo Avaliação/Frequência");
        System.out.println("4. Salvar Dados");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    private void modoAluno() {
        int opcao;
        do {
            System.out.println("\n=== MODO ALUNO ===");
            System.out.println("1. Cadastrar Aluno Normal");
            System.out.println("2. Cadastrar Aluno Especial");
            System.out.println("3. Listar Alunos");
            System.out.println("4. Matricular Aluno em Disciplina");
            System.out.println("5. Trancar Disciplina");
            System.out.println("6. Editar Aluno");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    cadastrarAluno(false);
                    break;
                case 2:
                    cadastrarAluno(true);
                    break;
                case 3:
                    listarAlunos();
                    break;
                case 4:
                    matricularAlunoEmDisciplina();
                    break;
                case 5:
                    trancarDisciplina();
                    break;
                case 6:
                    editarAluno();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    private void cadastrarAluno(boolean especial) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        
        // Verificar duplicidade
        for (Aluno aluno : alunos) {
            if (aluno.getMatricula().equals(matricula)) {
                System.out.println("Erro: Já existe um aluno com esta matrícula!");
                return;
            }
        }
        
        System.out.print("Curso: ");
        String curso = scanner.nextLine();
        
        Aluno novoAluno;
        if (especial) {
            novoAluno = new AlunoEspecial(nome, matricula, curso);
            System.out.println("Aluno Especial cadastrado com sucesso! Lembre-se: Alunos especiais podem cursar no máximo 2 disciplinas e não recebem notas.");
        } else {
            novoAluno = new Aluno(nome, matricula, curso);
            System.out.println("Aluno Normal cadastrado com sucesso!");
        }
        
        alunos.add(novoAluno);
    }
    
    private void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        
        System.out.println("\n=== LISTA DE ALUNOS ===");
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
            if (!aluno.getDisciplinasMatriculadas().isEmpty()) {
                System.out.println("  Disciplinas: " + aluno.getDisciplinasMatriculadas());
            }
            System.out.println();
        }
    }
    
    private void matricularAlunoEmDisciplina() {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        Turma turma = buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }
        
        // Verificar pré-requisitos
        Disciplina disciplina = buscarDisciplina(turma.getCodigoDisciplina());
        if (disciplina != null && !disciplina.getPreRequisitos().isEmpty()) {
            for (String preReq : disciplina.getPreRequisitos()) {
                if (!aluno.getDisciplinasMatriculadas().contains(preReq)) {
                    System.out.println("Erro: Aluno não possui o pré-requisito " + preReq);
                    return;
                }
            }
        }
        
        // Verificar se pode matricular (polimorfismo)
        if (!aluno.podeMatricular(turma.getCodigoDisciplina())) {
            if (aluno instanceof AlunoEspecial) {
                AlunoEspecial alunoEspecial = (AlunoEspecial) aluno;
                if (alunoEspecial.getDisciplinasMatriculadas().size() >= 2) {
                    System.out.println("Erro: Alunos especiais podem cursar no máximo 2 disciplinas!");
                } else {
                    System.out.println("Erro: Aluno não pode ser matriculado nesta disciplina!");
                }
            } else {
                System.out.println("Erro: Aluno não pode ser matriculado nesta disciplina!");
            }
            return;
        }
        
        if (turma.matricularAluno(matricula)) {
            aluno.matricularDisciplina(turma.getCodigoDisciplina());
            System.out.println("Aluno matriculado com sucesso!");
            if (aluno instanceof AlunoEspecial) {
                System.out.println("Lembre-se: Como aluno especial, você não receberá notas, apenas presença.");
            }
        } else {
            System.out.println("Erro: Turma sem vagas disponíveis!");
        }
    }
    
    private void trancarDisciplina() {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        
        if (aluno.getDisciplinasMatriculadas().isEmpty()) {
            System.out.println("Aluno não possui disciplinas matriculadas.");
            return;
        }
        
        System.out.println("Disciplinas matriculadas:");
        for (String disciplina : aluno.getDisciplinasMatriculadas()) {
            System.out.println("- " + disciplina);
        }
        
        System.out.print("Código da disciplina a trancar: ");
        String codigoDisciplina = scanner.nextLine();
        
        if (aluno.getDisciplinasMatriculadas().contains(codigoDisciplina)) {
            aluno.trancarDisciplina(codigoDisciplina);
            System.out.println("Disciplina trancada com sucesso!");
        } else {
            System.out.println("Aluno não está matriculado nesta disciplina!");
        }
    }
    
    private void editarAluno() {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        
        System.out.println("Dados atuais: " + aluno);
        System.out.print("Novo nome (enter para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            aluno.setNome(novoNome);
        }
        
        System.out.print("Novo curso (enter para manter): ");
        String novoCurso = scanner.nextLine();
        if (!novoCurso.trim().isEmpty()) {
            aluno.setCurso(novoCurso);
        }
        
        System.out.println("Aluno editado com sucesso!");
    }
    
    private void modoDisciplinaTurma() {
        int opcao;
        do {
            System.out.println("\n=== MODO DISCIPLINA/TURMA ===");
            System.out.println("1. Cadastrar Disciplina");
            System.out.println("2. Cadastrar Professor");
            System.out.println("3. Criar Turma");
            System.out.println("4. Listar Disciplinas");
            System.out.println("5. Listar Turmas");
            System.out.println("6. Listar Alunos por Turma");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    cadastrarDisciplina();
                    break;
                case 2:
                    cadastrarProfessor();
                    break;
                case 3:
                    criarTurma();
                    break;
                case 4:
                    listarDisciplinas();
                    break;
                case 5:
                    listarTurmas();
                    break;
                case 6:
                    listarAlunosPorTurma();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    private void cadastrarDisciplina() {
        System.out.print("Código da disciplina: ");
        String codigo = scanner.nextLine();
        
        // Verificar duplicidade
        for (Disciplina disc : disciplinas) {
            if (disc.getCodigo().equals(codigo)) {
                System.out.println("Erro: Já existe uma disciplina com este código!");
                return;
            }
        }
        
        System.out.print("Nome da disciplina: ");
        String nome = scanner.nextLine();
        
        System.out.print("Carga horária: ");
        int cargaHoraria = scanner.nextInt();
        scanner.nextLine();
        
        Disciplina disciplina = new Disciplina(codigo, nome, cargaHoraria);
        
        System.out.print("Possui pré-requisitos? (s/n): ");
        String resposta = scanner.nextLine();
        
        if (resposta.toLowerCase().equals("s")) {
            System.out.println("Digite os códigos dos pré-requisitos (separados por vírgula):");
            String preReqs = scanner.nextLine();
            
            for (String preReq : preReqs.split(",")) {
                disciplina.adicionarPreRequisito(preReq.trim());
            }
        }
        
        disciplinas.add(disciplina);
        System.out.println("Disciplina cadastrada com sucesso!");
    }
    
    private void cadastrarProfessor() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        
        // Verificar duplicidade
        for (Professor prof : professores) {
            if (prof.getMatricula().equals(matricula)) {
                System.out.println("Erro: Já existe um professor com esta matrícula!");
                return;
            }
        }
        
        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();
        
        Professor professor = new Professor(nome, matricula, departamento);
        professores.add(professor);
        System.out.println("Professor cadastrado com sucesso!");
    }
    
    private void criarTurma() {
        if (disciplinas.isEmpty()) {
            System.out.println("Erro: Cadastre disciplinas primeiro!");
            return;
        }
        
        if (professores.isEmpty()) {
            System.out.println("Erro: Cadastre professores primeiro!");
            return;
        }
        
        System.out.print("Código da turma: ");
        String codigo = scanner.nextLine();
        
        // Verificar duplicidade
        for (Turma turma : turmas) {
            if (turma.getCodigo().equals(codigo)) {
                System.out.println("Erro: Já existe uma turma com este código!");
                return;
            }
        }
        
        System.out.println("Disciplinas disponíveis:");
        for (Disciplina disc : disciplinas) {
            System.out.println("- " + disc);
        }
        
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        
        Disciplina disciplina = buscarDisciplina(codigoDisciplina);
        if (disciplina == null) {
            System.out.println("Disciplina não encontrada!");
            return;
        }
        
        System.out.println("Professores disponíveis:");
        for (Professor prof : professores) {
            System.out.println("- " + prof);
        }
        
        System.out.print("Matrícula do professor responsável: ");
        String matriculaProfessor = scanner.nextLine();
        
        Professor professor = buscarProfessor(matriculaProfessor);
        if (professor == null) {
            System.out.println("Professor não encontrado!");
            return;
        }
        
        System.out.print("Semestre (ex: 2024.1): ");
        String semestre = scanner.nextLine();
        
        System.out.println("Forma de avaliação:");
        System.out.println("1. Tipo 1: (P1 + P2 + P3 + L + S) / 5");
        System.out.println("2. Tipo 2: (P1 + P2*2 + P3*3 + L + S) / 8");
        System.out.print("Escolha (1 ou 2): ");
        int tipoAvaliacao = scanner.nextInt();
        scanner.nextLine();
        
        Turma.TipoAvaliacao formaAvaliacao = (tipoAvaliacao == 1) ? 
            Turma.TipoAvaliacao.TIPO1 : Turma.TipoAvaliacao.TIPO2;
        
        System.out.print("É presencial? (s/n): ");
        boolean presencial = scanner.nextLine().toLowerCase().equals("s");
        
        String sala = null;
        if (presencial) {
            System.out.print("Sala: ");
            sala = scanner.nextLine();
        }
        
        System.out.print("Horário: ");
        String horario = scanner.nextLine();
        
        System.out.print("Capacidade máxima: ");
        int capacidade = scanner.nextInt();
        scanner.nextLine();
        
        Turma turma = new Turma(codigo, codigoDisciplina, matriculaProfessor, 
                               semestre, formaAvaliacao, presencial, sala, horario, capacidade);
        
        turmas.add(turma);
        disciplina.adicionarTurma(turma);
        professor.adicionarTurma(codigo);
        
        System.out.println("Turma criada com sucesso!");
    }
    
    private void listarDisciplinas() {
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina cadastrada.");
            return;
        }
        
        System.out.println("\n=== LISTA DE DISCIPLINAS ===");
        for (Disciplina disciplina : disciplinas) {
            System.out.println(disciplina);
            if (!disciplina.getPreRequisitos().isEmpty()) {
                System.out.println("  Pré-requisitos: " + disciplina.getPreRequisitos());
            }
            System.out.println("  Turmas: " + disciplina.getTurmas().size());
            System.out.println();
        }
    }
    
    private void listarTurmas() {
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
            return;
        }
        
        System.out.println("\n=== LISTA DE TURMAS ===");
        for (Turma turma : turmas) {
            System.out.println(turma);
            System.out.println("  Vagas: " + turma.getAlunosMatriculados().size() + 
                             "/" + turma.getCapacidadeMaxima());
            System.out.println("  Forma de avaliação: " + turma.getFormaAvaliacao());
            System.out.println();
        }
    }
    
    private void listarAlunosPorTurma() {
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        Turma turma = buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }
        
        System.out.println("\n=== ALUNOS DA TURMA " + codigoTurma + " ===");
        System.out.println("Disciplina: " + turma.getCodigoDisciplina());
        System.out.println("Professor: " + turma.getProfessorResponsavel());
        System.out.println();
        
        if (turma.getAlunosMatriculados().isEmpty()) {
            System.out.println("Nenhum aluno matriculado.");
        } else {
            for (String matricula : turma.getAlunosMatriculados()) {
                Aluno aluno = buscarAluno(matricula);
                if (aluno != null) {
                    System.out.println("- " + aluno);
                }
            }
        }
    }
    
    private void modoAvaliacaoFrequencia() {
        int opcao;
        do {
            System.out.println("\n=== MODO AVALIAÇÃO/FREQUÊNCIA ===");
            System.out.println("1. Lançar Notas");
            System.out.println("2. Lançar Presença");
            System.out.println("3. Calcular Médias e Status");
            System.out.println("4. Relatório por Turma");
            System.out.println("5. Relatório por Disciplina");
            System.out.println("6. Relatório por Professor");
            System.out.println("7. Boletim do Aluno");
            System.out.println("8. Boletim Detalhado do Aluno");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    lancarNotas();
                    break;
                case 2:
                    lancarPresenca();
                    break;
                case 3:
                    calcularMediasStatus();
                    break;
                case 4:
                    relatorioPorTurma();
                    break;
                case 5:
                    relatorioPorDisciplina();
                    break;
                case 6:
                    relatorioPorProfessor();
                    break;
                case 7:
                    boletimAluno(false);
                    break;
                case 8:
                    boletimAluno(true);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    private void lancarNotas() {
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        Turma turma = buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }
        
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }

        if (aluno instanceof AlunoEspecial) {
            System.out.println("Erro: Alunos especiais não recebem notas!");
            return;
        }
        
        if (!turma.getAlunosMatriculados().contains(matricula)) {
            System.out.println("Aluno não está matriculado nesta turma!");
            return;
        }
        
        if (aluno.getDisciplinasMatriculadas().isEmpty()) {
            System.out.println("Aluno não possui disciplinas matriculadas.");
            return;
        }
        
        System.out.println("Disciplinas do aluno:");
        for (String disciplina : aluno.getDisciplinasMatriculadas()) {
            System.out.println("- " + disciplina);
        }
        
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        
        if (!aluno.getDisciplinasMatriculadas().contains(codigoDisciplina)) {
            System.out.println("Aluno não está matriculado nesta disciplina!");
            return;
        }
        
        System.out.println("Tipos de avaliação: P1, P2, P3, L, S");
        System.out.print("Tipo de avaliação: ");
        String tipo = scanner.nextLine().toUpperCase();
        
        System.out.print("Nota (0-10): ");
        double nota = scanner.nextDouble();
        scanner.nextLine();
        
        if (nota < 0 || nota > 10) {
            System.out.println("Nota deve estar entre 0 e 10!");
            return;
        }
        
        aluno.lancarNota(codigoDisciplina, tipo, nota);
        System.out.println("Nota lançada com sucesso!");
    }
    
    private void lancarPresenca() {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        
        if (aluno.getDisciplinasMatriculadas().isEmpty()) {
            System.out.println("Aluno não possui disciplinas matriculadas.");
            return;
        }
        
        System.out.println("Disciplinas do aluno:");
        for (String disciplina : aluno.getDisciplinasMatriculadas()) {
            System.out.println("- " + disciplina);
        }
        
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        
        if (!aluno.getDisciplinasMatriculadas().contains(codigoDisciplina)) {
            System.out.println("Aluno não está matriculado nesta disciplina!");
            return;
        }
        
        System.out.print("Número de presenças: ");
        int presencas = scanner.nextInt();
        scanner.nextLine();
        
        if (presencas < 0) {
            System.out.println("Número de presenças deve ser positivo!");
            return;
        }
        
        aluno.lancarPresenca(codigoDisciplina, presencas);
        System.out.println("Presença lançada com sucesso!");
    }
    
    private void calcularMediasStatus() {
        System.out.println("Calculando médias e status de todos os alunos...");
        
        avaliacoes.clear();
        
        for (Aluno aluno : alunos) {
            for (String disciplina : aluno.getDisciplinasMatriculadas()) {
                // Encontrar turma da disciplina
                Turma turma = null;
                for (Turma t : turmas) {
                    if (t.getCodigoDisciplina().equals(disciplina) && 
                        t.getAlunosMatriculados().contains(aluno.getMatricula())) {
                        turma = t;
                        break;
                    }
                }
                
                if (turma != null) {
                    AvaliacaoFrequencia avaliacao = new AvaliacaoFrequencia(
                        aluno.getMatricula(), turma.getCodigo());
                    
                    // Calcular média (apenas para alunos normais)
                    if (!(aluno instanceof AlunoEspecial)) {
                        Map<String, Double> notas = aluno.getNotas().get(disciplina);
                        if (notas != null && !notas.isEmpty()) {
                            double media = turma.calcularMediaFinal(notas);
                            avaliacao.setMediaFinal(media);
                        }
                    }
                    
                    // Definir presenças
                    Integer presencas = aluno.getPresencas().get(disciplina);
                    if (presencas != null) {
                        avaliacao.setPresencas(presencas);
                    }
                    
                    avaliacoes.add(avaliacao);
                }
            }
        }
        
        System.out.println("Cálculos realizados com sucesso!");
    }
    
    private void relatorioPorTurma() {
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        Turma turma = buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }
        
        System.out.println("\n=== RELATÓRIO DA TURMA " + codigoTurma + " ===");
        System.out.println("Disciplina: " + turma.getCodigoDisciplina());
        System.out.println("Professor: " + turma.getProfessorResponsavel());
        System.out.println("Semestre: " + turma.getSemestre());
        System.out.println("Modalidade: " + (turma.isPresencial() ? "Presencial" : "Remota"));
        System.out.println();
        
        boolean encontrouAvaliacoes = false;
        for (AvaliacaoFrequencia avaliacao : avaliacoes) {
            if (avaliacao.getCodigoTurma().equals(codigoTurma)) {
                System.out.println(avaliacao);
                encontrouAvaliacoes = true;
            }
        }
        
        if (!encontrouAvaliacoes) {
            System.out.println("Nenhuma avaliação encontrada. Execute 'Calcular Médias e Status' primeiro.");
        }
    }
    
    private void relatorioPorDisciplina() {
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        
        Disciplina disciplina = buscarDisciplina(codigoDisciplina);
        if (disciplina == null) {
            System.out.println("Disciplina não encontrada!");
            return;
        }
        
        System.out.println("\n=== RELATÓRIO DA DISCIPLINA " + codigoDisciplina + " ===");
        System.out.println("Nome: " + disciplina.getNome());
        System.out.println("Carga Horária: " + disciplina.getCargaHoraria() + "h");
        System.out.println();
        
        // Listar todas as turmas da disciplina
        for (Turma turma : turmas) {
            if (turma.getCodigoDisciplina().equals(codigoDisciplina)) {
                System.out.println("--- Turma " + turma.getCodigo() + " ---");
                System.out.println("Professor: " + turma.getProfessorResponsavel());
                System.out.println("Semestre: " + turma.getSemestre());
                
                for (AvaliacaoFrequencia avaliacao : avaliacoes) {
                    if (avaliacao.getCodigoTurma().equals(turma.getCodigo())) {
                        System.out.println("  " + avaliacao);
                    }
                }
                System.out.println();
            }
        }
    }
    
    private void relatorioPorProfessor() {
        System.out.print("Matrícula do professor: ");
        String matriculaProfessor = scanner.nextLine();
        
        Professor professor = buscarProfessor(matriculaProfessor);
        if (professor == null) {
            System.out.println("Professor não encontrado!");
            return;
        }
        
        System.out.println("\n=== RELATÓRIO DO PROFESSOR " + professor.getNome() + " ===");
        System.out.println("Departamento: " + professor.getDepartamento());
        System.out.println();
        
        // Listar todas as turmas do professor
        for (String codigoTurma : professor.getTurmasResponsavel()) {
            Turma turma = buscarTurma(codigoTurma);
            if (turma != null) {
                System.out.println("--- Turma " + codigoTurma + " ---");
                System.out.println("Disciplina: " + turma.getCodigoDisciplina());
                System.out.println("Semestre: " + turma.getSemestre());
                
                for (AvaliacaoFrequencia avaliacao : avaliacoes) {
                    if (avaliacao.getCodigoTurma().equals(codigoTurma)) {
                        System.out.println("  " + avaliacao);
                    }
                }
                System.out.println();
            }
        }
    }
    
    private void boletimAluno(boolean detalhado) {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = buscarAluno(matricula);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        
        System.out.print("Semestre (deixe vazio para todos): ");
        String semestreFiltro = scanner.nextLine();
        
        System.out.println("\n=== BOLETIM DO ALUNO ===");
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("Matrícula: " + aluno.getMatricula());
        System.out.println("Curso: " + aluno.getCurso());
        System.out.println("Tipo: " + aluno.getTipo());
        System.out.println();
        
        Map<String, List<AvaliacaoFrequencia>> avaliacoesPorSemestre = new HashMap<>();
        
        // Organizar avaliações por semestre
        for (AvaliacaoFrequencia avaliacao : avaliacoes) {
            if (avaliacao.getMatriculaAluno().equals(matricula)) {
                Turma turma = buscarTurma(avaliacao.getCodigoTurma());
                if (turma != null) {
                    String semestre = turma.getSemestre();
                    
                    if (semestreFiltro.isEmpty() || semestre.equals(semestreFiltro)) {
                        avaliacoesPorSemestre.computeIfAbsent(semestre, k -> new ArrayList<>())
                                           .add(avaliacao);
                    }
                }
            }
        }
        
        if (avaliacoesPorSemestre.isEmpty()) {
            System.out.println("Nenhuma avaliação encontrada para este aluno.");
            return;
        }
        
        // Exibir boletim por semestre
        for (Map.Entry<String, List<AvaliacaoFrequencia>> entry : avaliacoesPorSemestre.entrySet()) {
            String semestre = entry.getKey();
            List<AvaliacaoFrequencia> avaliacoesSemestre = entry.getValue();
            
            System.out.println("=== SEMESTRE " + semestre + " ===");
            
            for (AvaliacaoFrequencia avaliacao : avaliacoesSemestre) {
                Turma turma = buscarTurma(avaliacao.getCodigoTurma());
                if (turma != null) {
                    Disciplina disciplina = buscarDisciplina(turma.getCodigoDisciplina());
                    
                    System.out.println("Disciplina: " + (disciplina != null ? disciplina.getNome() : turma.getCodigoDisciplina()));
                    
                    if (detalhado) {
                        Professor professor = buscarProfessor(turma.getProfessorResponsavel());
                        System.out.println("  Professor: " + (professor != null ? professor.getNome() : turma.getProfessorResponsavel()));
                        System.out.println("  Modalidade: " + (turma.isPresencial() ? "Presencial" : "Remota"));
                        System.out.println("  Carga Horária: " + (disciplina != null ? disciplina.getCargaHoraria() : "N/A") + "h");
                    }
                    
                    if (aluno instanceof AlunoEspecial) {
                        System.out.println("  Frequência: " + String.format("%.1f", avaliacao.getFrequencia()) + "%");
                        System.out.println("  Status: " + (avaliacao.getFrequencia() >= 75 ? "APROVADO" : "REPROVADO POR FALTA"));
                    } else {
                        System.out.println("  Média Final: " + String.format("%.2f", avaliacao.getMediaFinal()));
                        System.out.println("  Frequência: " + String.format("%.1f", avaliacao.getFrequencia()) + "%");
                        System.out.println("  Status: " + avaliacao.calcularStatus());
                    }
                    System.out.println();
                }
            }
        }
    }
    
    // Métodos auxiliares de busca
    private Aluno buscarAluno(String matricula) {
        for (Aluno aluno : alunos) {
            if (aluno.getMatricula().equals(matricula)) {
                return aluno;
            }
        }
        return null;
    }
    
    private Professor buscarProfessor(String matricula) {
        for (Professor professor : professores) {
            if (professor.getMatricula().equals(matricula)) {
                return professor;
            }
        }
        return null;
    }
    
    private Disciplina buscarDisciplina(String codigo) {
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getCodigo().equals(codigo)) {
                return disciplina;
            }
        }
        return null;
    }
    
    private Turma buscarTurma(String codigo) {
        for (Turma turma : turmas) {
            if (turma.getCodigo().equals(codigo)) {
                return turma;
            }
        }
        return null;
    }
    
    // Métodos de persistência
    private void carregarDados() {
        alunos = GerenciadorDados.carregarAlunos();
        professores = GerenciadorDados.carregarProfessores();
        disciplinas = GerenciadorDados.carregarDisciplinas();
        turmas = GerenciadorDados.carregarTurmas();
        avaliacoes = GerenciadorDados.carregarAvaliacoes();
        // Outros carregamentos podem ser implementados similarmente
        System.out.println("Dados carregados: " + alunos.size() + " alunos");
    }
    
    private void salvarDados() {
        GerenciadorDados.salvarAlunos(alunos);
        GerenciadorDados.salvarProfessores(professores);
        GerenciadorDados.salvarDisciplinas(disciplinas);
        GerenciadorDados.salvarTurmas(turmas);
        GerenciadorDados.salvarAvaliacoes(avaliacoes);
    }
    
    // Método main
    public static void main(String[] args) {
        SistemaAcademico sistema = new SistemaAcademico();
        sistema.executar();
    }
}