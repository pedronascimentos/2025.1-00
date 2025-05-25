package model;

import java.io.Serializable;
import java.util.*;

public class Turma implements Serializable {
    private String codigo;
    private String codigoDisciplina;
    private String professorResponsavel;
    private String semestre;
    private TipoAvaliacao formaAvaliacao;
    private boolean presencial;
    private String sala;
    private String horario;
    private int capacidadeMaxima;
    private List<String> alunosMatriculados;
    
    public enum TipoAvaliacao {
        TIPO1, // (P1 + P2 + P3 + L + S) / 5
        TIPO2  // (P1 + P2*2 + P3*3 + L + S) / 8
    }
    
    public Turma(String codigo, String codigoDisciplina, String professorResponsavel, 
                 String semestre, TipoAvaliacao formaAvaliacao, boolean presencial,
                 String sala, String horario, int capacidadeMaxima) {
        this.codigo = codigo;
        this.codigoDisciplina = codigoDisciplina;
        this.professorResponsavel = professorResponsavel;
        this.semestre = semestre;
        this.formaAvaliacao = formaAvaliacao;
        this.presencial = presencial;
        this.sala = presencial ? sala : null;
        this.horario = horario;
        this.capacidadeMaxima = capacidadeMaxima;
        this.alunosMatriculados = new ArrayList<>();
    }
    
    // Encapsulamento
    public String getCodigo() { return codigo; }
    public String getCodigoDisciplina() { return codigoDisciplina; }
    public String getProfessorResponsavel() { return professorResponsavel; }
    public String getSemestre() { return semestre; }
    public TipoAvaliacao getFormaAvaliacao() { return formaAvaliacao; }
    public boolean isPresencial() { return presencial; }
    public String getSala() { return sala; }
    public String getHorario() { return horario; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public List<String> getAlunosMatriculados() { return new ArrayList<>(alunosMatriculados); }
    
    public boolean temVaga() {
        return alunosMatriculados.size() < capacidadeMaxima;
    }
    
    public boolean matricularAluno(String matriculaAluno) {
        if (temVaga() && !alunosMatriculados.contains(matriculaAluno)) {
            alunosMatriculados.add(matriculaAluno);
            return true;
        }
        return false;
    }
    
    // Polimorfismo - método para calcular média final
    public double calcularMediaFinal(Map<String, Double> notas) {
        double p1 = notas.getOrDefault("P1", 0.0);
        double p2 = notas.getOrDefault("P2", 0.0);
        double p3 = notas.getOrDefault("P3", 0.0);
        double l = notas.getOrDefault("L", 0.0);
        double s = notas.getOrDefault("S", 0.0);
        
        switch (formaAvaliacao) {
            case TIPO1:
                return (p1 + p2 + p3 + l + s) / 5.0;
            case TIPO2:
                return (p1 + p2 * 2 + p3 * 3 + l + s) / 8.0;
            default:
                return 0.0;
        }
    }
    
    @Override
    public String toString() {
        return codigo + " - " + codigoDisciplina + " (" + semestre + ") - " + 
               "Prof: " + professorResponsavel + " - " + 
               (presencial ? "Presencial - Sala: " + sala : "Remota") +
               " - " + horario;
    }
}