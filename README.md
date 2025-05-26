# Sistema Acadêmico - FCTE

## Descrição do Projeto

Desenvolvimento de um sistema acadêmico para gerenciar alunos, disciplinas, professores, turmas, avaliações e frequência, utilizando os conceitos de orientação a objetos (herança, polimorfismo e encapsulamento) e persistência de dados em arquivos.

O enunciado do trabalho pode ser encontrado aqui:
- [Trabalho 1 - Sistema Acadêmico](https://github.com/lboaventura25/OO-T06_2025.1_UnB_FCTE/blob/main/trabalhos/ep1/README.md)

## Dados do Aluno

- **Nome completo:** Pedro Henrique Monteiro Nascimento
- **Matrícula:** 241011582
- **Curso:** Engenharias (3º Semestre)
- **Turma:** T06 2025.1

---

## Vídeo de Demonstração

- [Inserir o link para o vídeo no YouTube/Drive aqui]

---

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior
- Sistema operacional com terminal/prompt de comando

### Compilação e Execução

1. **Estrutura de pastas necessária:**
```
sistema-academico/
├── model/
│   ├── Pessoa.java
│   ├── Aluno.java
│   ├── AlunoEspecial.java
│   ├── Professor.java
│   ├── Disciplina.java
│   ├── Turma.java
│   └── AvaliacaoFrequencia.java
├── util/
│   └── GerenciadorDados.java
├── SistemaAcademico.java
├── compilar.bat (Windows)
├── compilar.sh (Linux/Mac)
└── dados/ (criada automaticamente)
```

2. **Para Windows:**
```batch
# Execute o arquivo compilar.bat
compilar.bat

# Ou compile manualmente:
javac -d . modelo/*.java util/*.java SistemaAcademico.java
java SistemaAcademico
```

3. **Para Linux/Mac:**
```bash
# Dê permissão e execute:
chmod +x compilar.sh
./compilar.sh

# Ou compile manualmente:
javac -d . modelo/*.java util/*.java SistemaAcademico.java
java SistemaAcademico
```

---

## Prints da Execução

1. Menu Principal:  
   ![Inserir Print 1](caminho/do/print1.png)

2. Cadastro de Aluno:  
   ![Inserir Print 2](caminho/do/print2.png)

3. Relatório de Frequência/Notas:  
   ![Inserir Print 3](caminho/do/print3.png)

---

## Principais Funcionalidades Implementadas

- [x] Cadastro, listagem, matrícula e trancamento de alunos (Normais e Especiais)
- [x] Cadastro de disciplinas e criação de turmas (presenciais e remotas)
- [x] Matrícula de alunos em turmas, respeitando vagas e pré-requisitos
- [x] Lançamento de notas e controle de presença
- [x] Cálculo de média final e verificação de aprovação/reprovação
- [x] Relatórios de desempenho acadêmico por aluno, turma e disciplina
- [x] Persistência de dados em arquivos (.txt ou .csv)
- [x] Tratamento de duplicidade de matrículas
- [x] Uso de herança, polimorfismo e encapsulamento

---

## Observações (Extras ou Dificuldades)

- [Espaço para o aluno comentar qualquer funcionalidade extra que implementou, dificuldades enfrentadas, ou considerações importantes.]

---

## Contato

- [nascimento.monteiro@aluno.unb.br]
