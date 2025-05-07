# Instruções de configuração

## Pré-requisitos

- Docker
- Docker Compose
- Git

## Inicialização do servidor

1. No terminal navegue até a pasta onde deseja clonar o projeto e execute os comandos

```bash
git clone https://github.com/pedro-scarelli/LoginSpringSecurity.git
cd LoginSpringSecurity
```

2. Já na pasta root do projeto, execute o comando para iniciar a API:

```bash
docker compose up --build
```
---

# Documentação

## Cadastro de usuário

```bash
curl --location --request POST 'http://localhost:8080/v1/user' \

--header 'Content-Type: application/json' \
--data '{
    "name": "{NOME_DO_USUARIO}",
    "email": "{EMAIL_DO_USUARIO}",
    "password": "{SENHA_DO_USUARIO}"
}'
```
***Esse método envia um e-mail para ativar o seu usuário***  
![Screenshot From 2025-05-07 19-09-07](https://github.com/user-attachments/assets/512f464a-dee8-4265-b9ae-5c91f74d7daa)
---
## Ativar usuário

```bash
curl --location --request GET 'http://localhost:8080/v1/user/activate/{ID_DO_USUARIO}'
```
---
## Login

```bash
curl --location --request POST 'http://localhost:8080/v1/auth/login' \

--header 'Content-Type: application/json' \
--data '{
    "email": "{EMAIL_DO_USUARIO}",
    "password": "{SENHA_DO_USUARIO}"
}'
```

### Caso queira logar em algum dos users criados pelo flyway a senha é: senha123
---
## Obter todos os usuários

### ROTA PARA ADMINS

- O método obter todos usuários funciona com paginação, substitua o 4 pelo número da página desejado e 2 pela quantidade de itens desejados.

```bash
curl --location --request GET 'http://localhost:8080/v1/user?page=4&items=2' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}'
```
---
## Obter usuário

```bash
curl --location --globoff --request GET 'http://localhost:8080/v1/user/{ID_DO_USUARIO}' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}'
```
---
## Atualizar usuário

```bash
curl --location --globoff --request PATCH 'http://localhost:8080/v1/user/{ID_DO_USUARIO}' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}' \
--data '{
    "name": "{NOME_DO_USUARIO}",
    "password": "{SENHA_DO_USUARIO}"
}'
```
---
## Deletar usuário

### O usuário apenas vai ser marcada como deletado e excluido das próximas buscas.

```bash
curl --location --globoff --request DELETE 'http://localhost:8080/v1/user/{ID_DO_USUARIO}' \
--header 'Authorization: Bearer {TOKEN_DO_USUARIO}'
```
---
## Ativar redefinição de senha
### Esse endpoint ativa a redefinição de senha para um usuário
```bash
curl --location --request POST 'http://localhost:8080/v1/auth/redefine-password/activate' \
--header 'Content-Type: application/json' \
--data '{
    "email": "{EMAIL_DO_USUARIO}"
}'
```
***E-mail enviado com código para redefinição de senha***
![Screenshot From 2025-05-07 19-10-34](https://github.com/user-attachments/assets/6201f6af-e628-4d66-b8e3-c4344ac99455)
---
## Redefinir senha

Pegue o código que você recebeu no endpoint que ativa a redefinição de senha e coloque no campo "otpCode"

```bash
curl --location --request PATCH 'http://localhost:8080/v1/auth/redefine-password' \
--header 'Content-Type: application/json' \
--data '{
    "otpCode": "{CODIGO_OTP}",
    "newPassword": "{NEW_PASSWORD}",
    "email": "{EMAIL_DO_USUARIO}"
}'
```
