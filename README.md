# Sistema Acadêmico FCTE

Sistema desenvolvido em Java para gerenciamento acadêmico da FCTE, permitindo controle de alunos, disciplinas, professores, turmas e avaliações.

## 📋 Funcionalidades

### Modo Aluno
- Cadastro de alunos normais e especiais
- Matrícula em disciplinas com verificação de pré-requisitos
- Controle de vagas por turma
- Trancamento de disciplinas
- Validação de duplicidade por matrícula

### Modo Disciplina/Turma
- Cadastro de disciplinas com pré-requisitos
- Cadastro de professores
- Criação de turmas (presenciais e remotas)
- Controle de capacidade e horários
- Duas formas de avaliação configuráveis

### Modo Avaliação/Frequência
- Lançamento de notas e presenças
- Cálculo automático de médias
- Relatórios por turma, disciplina e professor
- Boletins individuais por aluno
- Controle de aprovação (nota ≥ 5 e frequência ≥ 75%)

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior
- Sistema operacional com terminal/prompt de comando

### Compilação e Execução

1. **Estrutura de pastas necessária:**
```
sistema-academico/
├── modelo/
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

##