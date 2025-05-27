package model;

public class AlunoEspecial extends Aluno {
    private static final int MAX_DISCIPLINAS = 2;
    
    public AlunoEspecial(String nome, String matricula, String curso) {
        super(nome, matricula, curso);
    }
    
    @Override
    public String getTipo() {
        return "Aluno Especial";
    }
    
    @Override
    public boolean podeMatricular(String disciplina) {
        return super.podeMatricular(disciplina) && 
               getDisciplinasMatriculadas().size() < MAX_DISCIPLINAS;
    }
    
    @Override
    public void lancarNota(String disciplina, String tipo, double valor) {
        // Alunos especiais não recebem notas
    }
}