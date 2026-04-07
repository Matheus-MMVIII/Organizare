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
- `APP_PORT`: porta exposta pelo container da aplicacao. Padrao `8080`
- `ALLOWED_ORIGIN`: origem liberada para o frontend React. Padrao `http://localhost:5173`
- `MAX_REQUEST_BODY_BYTES`: limite do corpo JSON. Padrao `8192`
- `SERVER_BACKLOG`: fila maxima basica do servidor HTTP
- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `POSTGRES_PORT`
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

A aplicacao tenta ler primeiro as variaveis do sistema e, se elas nao existirem, usa o arquivo local `.env`.
No Docker Compose, os valores padrao tambem podem vir do arquivo `.env`, mas o projeto consegue subir usando os fallbacks definidos em `docker-compose.yml`.

Configuracao padrao deste projeto:

- `DB_URL=jdbc:postgresql://localhost:5432/organizare-db`
- `DB_USER=macinha`
- `DB_PASSWORD=99146632`

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

### Docker Compose

Para subir API + PostgreSQL com os valores padrao:

```bash
docker compose up --build
```

Para derrubar os containers:

```bash
docker compose down
```

A API fica disponivel em `http://localhost:8080` por padrao e o healthcheck interno usa `http://127.0.0.1:8080/health`.

### Frontend React

O frontend foi criado em `frontend/` com Vite + React.

Instale as dependencias:

```bash
cd frontend
npm install
```

Rode em desenvolvimento:

```bash
npm run dev
```

Por padrao o Vite sobe em `http://localhost:5173` e faz proxy de `/api` para `http://localhost:8080`.

Se quiser apontar para outra URL da API, rode assim:

```bash
VITE_API_URL=http://localhost:8080 npm run dev
```

Para gerar build:

```bash
npm run build
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
- `GET /api/buy`
- `GET /api/buy/{id}`
- `POST /api/buy`
- `PUT /api/buy/{id}`
- `DELETE /api/buy/{id}`

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

`POST /api/buy`

```json
{
  "userId": 1,
  "clothingId": 2,
  "quantity": 3
}
```
