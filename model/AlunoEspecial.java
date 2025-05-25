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
    
    // Polimorfismo - sobrescrevendo método da classe pai
    @Override
    public boolean podeMatricular(String disciplina) {
        return super.podeMatricular(disciplina) && 
               getDisciplinasMatriculadas().size() < MAX_DISCIPLINAS;
    }
    
    // Aluno especial não recebe notas, apenas presença
    @Override
    public void lancarNota(String disciplina, String tipo, double valor) {
        // Não faz nada - alunos especiais não recebem notas
    }
}