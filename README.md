## Organizare

Projeto Java com foco em cadastro de usuarios e produtos de roupa com persistencia em PostgreSQL.

## Estrutura

- `src/main/java/com/organizare/App.java`: ponto de entrada da aplicacao
- `src/main/java/com/organizare/config`: configuracao de conexao com o banco
- `src/main/java/com/organizare/model`: entidades de dominio
- `src/main/java/com/organizare/repository`: acesso a dados por entidade
- `lib/postgresql-42.7.3.jar`: driver JDBC do PostgreSQL

## Variaveis de ambiente

Configure antes de rodar a aplicacao:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

## Melhorias aplicadas

- separacao por responsabilidade
- nomes de classes mais consistentes
- `package` alinhado com a estrutura de pastas
- fechamento seguro de conexoes e statements com `try-with-resources`
