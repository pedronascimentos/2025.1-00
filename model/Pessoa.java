package model;

import java.io.Serializable;

public abstract class Pessoa implements Serializable {
    protected String nome;
    protected String matricula;
    
    public Pessoa(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }
    
    // Encapsulamento - getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    // Método abstrato para polimorfismo
    public abstract String getTipo();
    
    @Override
    public String toString() {
        return "Nome: " + nome + ", Matrícula: " + matricula;
    }
}