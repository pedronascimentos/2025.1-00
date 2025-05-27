package model;

import java.io.Serializable;

public class AvaliacaoFrequencia implements Serializable {
    private String matriculaAluno;
    private String codigoTurma;
    private double mediaFinal;
    private int presencas;
    private int totalAulas;
    private StatusAluno status;
    
    public enum StatusAluno {
        APROVADO,
        REPROVADO_NOTA,
        REPROVADO_FALTA
    }
    
    public AvaliacaoFrequencia(String matriculaAluno, String codigoTurma) {
        this.matriculaAluno = matriculaAluno;
        this.codigoTurma = codigoTurma;
        this.totalAulas = 60;
    }
    
    public String getMatriculaAluno() { return matriculaAluno; }
    public String getCodigoTurma() { return codigoTurma; }
    public double getMediaFinal() { return mediaFinal; }
    public void setMediaFinal(double mediaFinal) { this.mediaFinal = mediaFinal; }
    public int getPresencas() { return presencas; }
    public void setPresencas(int presencas) { this.presencas = presencas; }
    public int getTotalAulas() { return totalAulas; }
    public StatusAluno getStatus() { return status; }
    
    public double getFrequencia() {
        return totalAulas > 0 ? (double) presencas / totalAulas * 100 : 0;
    }
    
    public StatusAluno calcularStatus() {
        double frequencia = getFrequencia();
        
        if (frequencia < 75.0) {
            status = StatusAluno.REPROVADO_FALTA;
        } else if (mediaFinal < 5.0) {
            status = StatusAluno.REPROVADO_NOTA;
        } else {
            status = StatusAluno.APROVADO;
        }
        
        return status;
    }
    
    @Override
    public String toString() {
        return "Aluno: " + matriculaAluno + " - Turma: " + codigoTurma + 
               " - Média: " + String.format("%.2f", mediaFinal) + 
               " - Frequência: " + String.format("%.1f", getFrequencia()) + "%" +
               " - Status: " + calcularStatus();
    }
}