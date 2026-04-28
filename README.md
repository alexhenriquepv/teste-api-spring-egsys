# API egSys

API REST para gerenciamento de tarefas. 

## Requisitos

- Java 21 ou superior;
- Maven 4.1.0 ou superior (caso deseje rodar a aplicação via linha de comando);

> **Nota:** Se você for utilizar uma IDE (como IntelliJ IDEA, Eclipse ou VS Code), não é obrigatório ter o Maven instalado globalmente na sua máquina, pois a IDE já gerencia as dependências utilizando sua versão embutida.

## Como executar

Você tem duas formas de executar a aplicação:

### Opção 1: Via IDE (Recomendado)
1. Importe/abra a pasta do projeto (`egsys`) na sua IDE favorita.
2. Aguarde a IDE baixar as dependências automaticamente.
3. Navegue até o arquivo `TarefasApplication.java` (em `src/main/java/com/example/tarefas/TarefasApplication.java`).
4. Execute a classe (Run).

### Opção 2: Via Linha de Comando (Terminal)
1. Certifique-se de que o Maven está instalado e configurado nas variáveis de ambiente.
2. Abra um terminal na pasta raiz do projeto (onde está localizado o arquivo `pom.xml`).
3. Execute o comando abaixo para iniciar a aplicação:

```bash
mvn spring-boot:run
```

Independente da opção escolhida, a aplicação estará disponível em: `http://localhost:8080`.

## Endpoints Principais

A API responde nos seguintes endpoints principais:

### Categorias (`/categorias`)

- `GET /categorias`: Lista todas as categorias.
  - Exemplo de resposta:
    ```json
    [
      {
        "id": 1,
        "descricao": "Trabalho"
      }
    ]
    ```
- `GET /categorias/{id}`: Busca uma categoria pelo ID.
- `POST /categorias`: Cria uma nova categoria.
  - Exemplo de corpo (JSON):
    ```json
    {
      "descricao": "Trabalho"
    }
    ```
- `PUT /categorias/{id}`: Atualiza uma categoria existente.
- `DELETE /categorias/{id}`: Deleta uma categoria pelo ID.

### Tarefas (`/tarefas`)

- `GET /tarefas`: Lista todas as tarefas.
  - Exemplo de resposta:
    ```json
    [
      {
        "id": 1,
        "titulo": "Finalizar relatório",
        "descricao": "Escrever a conclusão e enviar.",
        "categoria": {
          "id": 1,
          "descricao": "Trabalho"
        },
        "dataHora": "2026-05-01T14:30:00"
      }
    ]
    ```
- `GET /tarefas/{id}`: Busca uma tarefa pelo ID.
- `POST /tarefas`: Cria uma nova tarefa. **Atenção:** A categoria é obrigatória.
  - Exemplo de corpo (JSON):
    ```json
    {
      "titulo": "Finalizar relatório",
      "descricao": "Escrever a conclusão e enviar.",
      "dataHora": "2026-05-01T14:30:00",
      "categoria": {
        "id": 1
      }
    }
    ```
- `PUT /tarefas/{id}`: Atualiza uma tarefa existente.
- `DELETE /tarefas/{id}`: Deleta uma tarefa pelo ID.

## Banco de Dados H2 (Console)

O console do H2 está habilitado para facilitar a visualização dos dados.
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:tarefasdb`
- **User Name:** `egsys`
- **Password:** `egsys`

## Documentação da API (Swagger)

A documentação interativa da API está configurada através do Swagger/OpenAPI.
- **Acesse em:** `http://localhost:8080/swagger-ui.html`
- **JSON Definition:** `http://localhost:8080/v3/api-docs`
