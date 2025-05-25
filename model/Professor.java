package model;

import java.util.*;

public class Professor extends Pessoa {
    private String departamento;
    private List<String> turmasResponsavel;
    
    public Professor(String nome, String matricula, String departamento) {
        super(nome, matricula);
        this.departamento = departamento;
        this.turmasResponsavel = new ArrayList<>();
    }
    
    @Override
    public String getTipo() {
        return "Professor";
    }
    
    // Encapsulamento
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public List<String> getTurmasResponsavel() { return new ArrayList<>(turmasResponsavel); }
    
    public void adicionarTurma(String codigoTurma) {
        if (!turmasResponsavel.contains(codigoTurma)) {
            turmasResponsavel.add(codigoTurma);
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Departamento: " + departamento;
    }
}