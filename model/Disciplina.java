package model;

import java.io.Serializable;
import java.util.*;

public class Disciplina implements Serializable {
    private String codigo;
    private String nome;
    private int cargaHoraria;
    private List<String> preRequisitos;
    private List<Turma> turmas;
    
    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.preRequisitos = new ArrayList<>();
        this.turmas = new ArrayList<>();
    }
    
    // Encapsulamento
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getCargaHoraria() { return cargaHoraria; }
    public List<String> getPreRequisitos() { return new ArrayList<>(preRequisitos); }
    public List<Turma> getTurmas() { return new ArrayList<>(turmas); }
    
    public void adicionarPreRequisito(String codigo) {
        if (!preRequisitos.contains(codigo)) {
            preRequisitos.add(codigo);
        }
    }
    
    public void adicionarTurma(Turma turma) {
        if (!turmas.contains(turma)) {
            turmas.add(turma);
        }
    }
    
    @Override
    public String toString() {
        return codigo + " - " + nome + " (" + cargaHoraria + "h)";
    }
}
