package model;

import java.util.*;

public class Aluno extends Pessoa {
    private String curso;
    private List<String> disciplinasMatriculadas;
    private Map<String, Map<String, Double>> notas; // disciplina -> {P1, P2, P3, L, S}
    private Map<String, Integer> presencas; // disciplina -> presencas
    private Set<String> disciplinasTrancadas;
    
    public Aluno(String nome, String matricula, String curso) {
        super(nome, matricula);
        this.curso = curso;
        this.disciplinasMatriculadas = new ArrayList<>();
        this.notas = new HashMap<>();
        this.presencas = new HashMap<>();
        this.disciplinasTrancadas = new HashSet<>();
    }
    
    @Override
    public String getTipo() {
        return "Aluno Normal";
    }
    
    // Encapsulamento
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public List<String> getDisciplinasMatriculadas() { return new ArrayList<>(disciplinasMatriculadas); }
    public Map<String, Map<String, Double>> getNotas() { return new HashMap<>(notas); }
    public Map<String, Integer> getPresencas() { return new HashMap<>(presencas); }
    public Set<String> getDisciplinasTrancadas() { return new HashSet<>(disciplinasTrancadas); }
    
    public void matricularDisciplina(String codigoDisciplina) {
        if (!disciplinasMatriculadas.contains(codigoDisciplina)) {
            disciplinasMatriculadas.add(codigoDisciplina);
            notas.put(codigoDisciplina, new HashMap<>());
            presencas.put(codigoDisciplina, 0);
        }
    }
    
    public void trancarDisciplina(String codigoDisciplina) {
        disciplinasMatriculadas.remove(codigoDisciplina);
        disciplinasTrancadas.add(codigoDisciplina);
        notas.remove(codigoDisciplina);
        presencas.remove(codigoDisciplina);
    }
    
    public void lancarNota(String disciplina, String tipo, double valor) {
        notas.computeIfAbsent(disciplina, k -> new HashMap<>()).put(tipo, valor);
    }
    
    public void lancarPresenca(String disciplina, int presenca) {
        presencas.put(disciplina, presenca);
    }
    
    // Polimorfismo - método que pode ser sobrescrito
    public boolean podeMatricular(String disciplina) {
        return !disciplinasMatriculadas.contains(disciplina) && 
               !disciplinasTrancadas.contains(disciplina);
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Curso: " + curso + ", Tipo: " + getTipo();
    }
}