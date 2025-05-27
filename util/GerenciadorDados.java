package util;

import model.*;
import java.io.*;
import java.util.*;

public class GerenciadorDados {
    private static final String ARQUIVO_ALUNOS = "dados/alunos.csv";
    private static final String ARQUIVO_PROFESSORES = "dados/professores.csv";
    private static final String ARQUIVO_DISCIPLINAS = "dados/disciplinas.csv";
    private static final String ARQUIVO_TURMAS = "dados/turmas.csv";
    private static final String ARQUIVO_AVALIACOES = "dados/avaliacoes.csv";
    
    // Polimorfismo - método genérico para salvar qualquer tipo de dados
    public static <T> void salvarDados(List<T> dados, String nomeArquivo) {
        try {
            new File("dados").mkdirs();
            FileWriter writer = new FileWriter(nomeArquivo);
            
            for (T item : dados) {
                writer.write(item.toString() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    public static void salvarAlunos(List<Aluno> alunos) {
        try {
            new File("dados").mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ALUNOS));
            
            for (Aluno aluno : alunos) {
                writer.println(aluno.getTipo() + ";" + aluno.getNome() + ";" + 
                              aluno.getMatricula() + ";" + aluno.getCurso());
                
                // Salvar disciplinas matriculadas
                writer.println("DISCIPLINAS:" + String.join(",", aluno.getDisciplinasMatriculadas()));
                
                // Salvar notas
                for (Map.Entry<String, Map<String, Double>> entry : aluno.getNotas().entrySet()) {
                    String disciplina = entry.getKey();
                    Map<String, Double> notas = entry.getValue();
                    writer.print("NOTAS:" + disciplina + ":");
                    for (Map.Entry<String, Double> nota : notas.entrySet()) {
                        writer.print(nota.getKey() + "=" + nota.getValue() + ",");
                    }
                    writer.println();
                }
                
                // Salvar presenças
                for (Map.Entry<String, Integer> entry : aluno.getPresencas().entrySet()) {
                    writer.println("PRESENCA:" + entry.getKey() + ":" + entry.getValue());
                }
                
                writer.println("---");
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar alunos: " + e.getMessage());
        }
    }
    
    public static List<Aluno> carregarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        
        try {
            File file = new File(ARQUIVO_ALUNOS);
            if (!file.exists()) return alunos;
            
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (linha.equals("---")) continue;
                
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Aluno aluno;
                    if (dados[0].equals("Aluno Especial")) {
                        aluno = new AlunoEspecial(dados[1], dados[2], dados[3]);
                    } else {
                        aluno = new Aluno(dados[1], dados[2], dados[3]);
                    }
                    
                    // Carregar dados adicionais
                    while (scanner.hasNextLine()) {
                        String linhaDados = scanner.nextLine();
                        if (linhaDados.equals("---")) break;
                        
                        if (linhaDados.startsWith("DISCIPLINAS:")) {
                            String disciplinas = linhaDados.substring(12);
                            if (!disciplinas.isEmpty()) {
                                for (String disc : disciplinas.split(",")) {
                                    aluno.matricularDisciplina(disc);
                                }
                            }
                        } else if (linhaDados.startsWith("NOTAS:")) {
                            String[] partes = linhaDados.substring(6).split(":");
                            if (partes.length >= 2) {
                                String disciplina = partes[0];
                                String[] notas = partes[1].split(",");
                                for (String nota : notas) {
                                    if (!nota.isEmpty()) {
                                        String[] nv = nota.split("=");
                                        if (nv.length == 2) {
                                            aluno.lancarNota(disciplina, nv[0], Double.parseDouble(nv[1]));
                                        }
                                    }
                                }
                            }
                        } else if (linhaDados.startsWith("PRESENCA:")) {
                            String[] partes = linhaDados.substring(9).split(":");
                            if (partes.length == 2) {
                                aluno.lancarPresenca(partes[0], Integer.parseInt(partes[1]));
                            }
                        }
                    }
                    
                    alunos.add(aluno);
                }
            }
            
            scanner.close();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar alunos: " + e.getMessage());
        }
        
        return alunos;
    }
    
    // Métodos similares para outros dados...
    public static void salvarProfessores(List<Professor> professores) {
        try {
            new File("dados").mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PROFESSORES));
            
            for (Professor professor : professores) {
                writer.println(professor.getNome() + ";" +
                             professor.getMatricula() + ";" +
                             professor.getDepartamento() + ";" +
                             String.join(",", professor.getTurmasResponsavel()));
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar professores: " + e.getMessage());
        }
    }
    
    public static void salvarDisciplinas(List<Disciplina> disciplinas) {
        try {
            new File("dados").mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_DISCIPLINAS));
            
            for (Disciplina disciplina : disciplinas) {
                writer.println(disciplina.getCodigo() + ";" +
                             disciplina.getNome() + ";" +
                             disciplina.getCargaHoraria() + ";" +
                             String.join(",", disciplina.getPreRequisitos()));
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar disciplinas: " + e.getMessage());
        }
    }
    
    public static void salvarTurmas(List<Turma> turmas) {
        try {
            new File("dados").mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_TURMAS));
            
            for (Turma turma : turmas) {
                writer.println(turma.getCodigo() + ";" +
                             turma.getCodigoDisciplina() + ";" +
                             turma.getProfessorResponsavel() + ";" +
                             turma.getSemestre() + ";" +
                             turma.getFormaAvaliacao() + ";" +
                             turma.isPresencial() + ";" +
                             turma.getSala() + ";" +
                             turma.getHorario() + ";" +
                             turma.getCapacidadeMaxima() + ";" +
                             String.join(",", turma.getAlunosMatriculados()));
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar turmas: " + e.getMessage());
        }
    }
    
    public static void salvarAvaliacoes(List<AvaliacaoFrequencia> avaliacoes) {
        try {
            new File("dados").mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_AVALIACOES));
            
            for (AvaliacaoFrequencia avaliacao : avaliacoes) {
                writer.println(avaliacao.getMatriculaAluno() + ";" + 
                             avaliacao.getCodigoTurma() + ";" +
                             avaliacao.getMediaFinal() + ";" +
                             avaliacao.getPresencas() + ";" +
                             avaliacao.getTotalAulas());
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar avaliações: " + e.getMessage());
        }
    }
    
    public static List<AvaliacaoFrequencia> carregarAvaliacoes() {
        List<AvaliacaoFrequencia> avaliacoes = new ArrayList<>();
        try {
            File file = new File(ARQUIVO_AVALIACOES);
            if (!file.exists() || file.length() == 0) return avaliacoes;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (dados.length >= 5) {
                    AvaliacaoFrequencia avaliacao = new AvaliacaoFrequencia(dados[0], dados[1]);
                    avaliacao.setMediaFinal(Double.parseDouble(dados[2]));
                    avaliacao.setPresencas(Integer.parseInt(dados[3]));
                    avaliacoes.add(avaliacao);
                }
            }
            scanner.close();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar avaliações: " + e.getMessage());
        }
        return avaliacoes;
    }

    public static List<Professor> carregarProfessores() {
        List<Professor> professores = new ArrayList<>();
        try {
            File file = new File(ARQUIVO_PROFESSORES);
            if (!file.exists() || file.length() == 0) return professores;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (dados.length >= 3) {
                    Professor professor = new Professor(dados[0], dados[1], dados[2]);
                    if (dados.length > 3 && !dados[3].isEmpty()) {
                        for (String turma : dados[3].split(",")) {
                            professor.adicionarTurma(turma);
                        }
                    }
                    professores.add(professor);
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar professores: " + e.getMessage());
        }
        return professores;
    }

    public static List<Disciplina> carregarDisciplinas() {
        List<Disciplina> disciplinas = new ArrayList<>();
        try {
            File file = new File(ARQUIVO_DISCIPLINAS);
            if (!file.exists() || file.length() == 0) return disciplinas;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (dados.length >= 3) {
                    Disciplina disciplina = new Disciplina(
                        dados[0],                    // código
                        dados[1],                    // nome
                        Integer.parseInt(dados[2])   // cargaHoraria
                    );
                    
                    // Carregar pré-requisitos se existirem
                    if (dados.length > 3 && !dados[3].isEmpty()) {
                        for (String preReq : dados[3].split(",")) {
                            disciplina.adicionarPreRequisito(preReq);
                        }
                    }
                    
                    disciplinas.add(disciplina);
                }
            }
            scanner.close();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar disciplinas: " + e.getMessage());
        }
        return disciplinas;
    }

    public static List<Turma> carregarTurmas() {
        List<Turma> turmas = new ArrayList<>();
        try {
            File file = new File(ARQUIVO_TURMAS);
            if (!file.exists() || file.length() == 0) return turmas;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (dados.length >= 9) {
                    Turma turma = new Turma(
                        dados[0],                    // código
                        dados[1],                    // codigoDisciplina
                        dados[2],                    // professorResponsavel
                        dados[3],                    // semestre
                        Turma.TipoAvaliacao.valueOf(dados[4]), // formaAvaliacao
                        Boolean.parseBoolean(dados[5]), // presencial
                        dados[6],                    // sala
                        dados[7],                    // horario
                        Integer.parseInt(dados[8])   // capacidadeMaxima
                    );
                    
                    // Se houver alunos matriculados (opcional)
                    if (dados.length > 9 && !dados[9].isEmpty()) {
                        for (String matricula : dados[9].split(",")) {
                            turma.matricularAluno(matricula);
                        }
                    }
                    
                    turmas.add(turma);
                }
            }
            scanner.close();
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Erro ao carregar turmas: " + e.getMessage());
        }
        return turmas;
    }
}