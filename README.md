## Organizare

Projeto Java puro com API HTTP para cadastro de usuarios e produtos de roupa com persistencia em PostgreSQL.

## Estrutura

- `src/main/java/com/organizare/App.java`: ponto de entrada da API HTTP
- `src/main/java/com/organizare/config`: configuracao de conexao com o banco
- `src/main/java/com/organizare/http`: servidor, rotas e utilitarios HTTP
- `src/main/java/com/organizare/model`: entidades de dominio
- `src/main/java/com/organizare/repository`: acesso a dados por entidade
- `src/main/java/com/organizare/service`: regras de negocio e validacao de fluxo
- `src/main/java/com/organizare/validation`: validacao de entrada
- `src/main/java/com/organizare/exception`: excecoes da API

## Variaveis de ambiente

- `PORT`: porta do servidor HTTP. Padrao `8080`
- `ALLOWED_ORIGIN`: origem liberada para o frontend React. Padrao `http://localhost:5173`
- `MAX_REQUEST_BODY_BYTES`: limite do corpo JSON. Padrao `8192`
- `SERVER_BACKLOG`: fila maxima basica do servidor HTTP
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

A aplicacao tenta ler primeiro as variaveis do sistema e, se elas nao existirem, usa o arquivo local `.env`.

## Como rodar

Compile com o driver JDBC no classpath:

```bash
mkdir -p bin
javac -cp lib/postgresql-42.7.3.jar -d bin $(find src/main/java -name '*.java')
```

Inicie a API:

```bash
java -cp "lib/postgresql-42.7.3.jar:bin" com.organizare.App
```

## Endpoints

- `GET /health`
- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`
- `GET /api/clothing`
- `GET /api/clothing/{id}`
- `POST /api/clothing`
- `PUT /api/clothing/{id}`
- `DELETE /api/clothing/{id}`

## Seguranca e endurecimento aplicados

- validacao de entrada no backend
- respostas JSON com codigos HTTP consistentes
- limite de tamanho do corpo da requisicao
- `PreparedStatement` para evitar SQL injection
- CORS controlado por variavel de ambiente
- headers de seguranca basicos nas respostas
- erro interno sem vazar stack trace para o cliente

## Exemplos de JSON

`POST /api/users`

```json
{
  "name": "Maria",
  "email": "maria@email.com",
  "cellPhone": "+55 11 99999-9999",
  "birthMonth": 5,
  "birthDay": 15
}
```

`POST /api/clothing`

```json
{
  "name": "Jaqueta Jeans",
  "price": 199.9,
  "stock": 12,
  "size": "M",
  "color": "Azul"
}
```
