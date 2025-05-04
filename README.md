User

# Instruções de Configuração

## Pré-requisitos

- Docker
- Docker Compose
- Git

## Inicialização do Servidor

1. No terminal navegue até a pasta onde deseja clonar o projeto e execute os comandos
```bash
git clone https://github.com/pedro-scarelli/LoginComSpringSecurity.git
cd LoginComSpringSecurity
```
3. Já na pasta root do projeto, execute o comando

```bash
docker compose up --build
```
para iniciar a api.

4. Se a porta 8080 não estiver disponível ele vai iniciar em outra porta e você devera mudar a porta depois do localhost: nas rotas.

# Documentação da API

## Cadastro de Usuário

```bash
curl --location 'http://localhost:8080/v1/user' \
--header 'Content-Type: application/json' \
--data '{
    "name": "{NOME_DO_USUARIO}",
    "email": "{EMAIL_DO_USUARIO}",
    "password": "{SENHA_DO_USUARIO}" 
}'
```

## Login
```bash
curl --location 'http://localhost:8080/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "email": "{EMAIL_DO_USUARIO}",
    "password": "{SENHA_DO_USUARIO}"
}'
```
### Caso queira logar em algum dos users criados pelo flyway a senha é: senha123

## Obter Todos os Usuários

- O método obter todos usuários funciona com paginação, substitua o 4 pelo número da página desejado e 2 pela quantidade de itens desejados.
```bash
curl --location 'http://localhost:8080/v1/user?page=4&items=2' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}'
```
## Obter Usuário
```bash
curl --location --globoff 'http://localhost:8080/v1/user/{ID_DO_USUARIO}' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}'
```
## Atualizar Usuário
```bash
curl --location --globoff --request PATCH 'http://localhost:8080/v1/user/{ID_DO_USUARIO}' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}' \
--data '{
    "name": "{NOME_DO_USUARIO}",
    "password": "{SENHA_DO_USUARIO}"
}'
```

## Excluir Usuário
```bash
curl --location --globoff --request DELETE 'http://localhost:8080/v1/user/{ID_DO_USUARIO}' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}'
```
