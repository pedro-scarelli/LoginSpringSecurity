**LoginSpringSecurity**

API de autenticação e gerenciamento de usuários desenvolvida com Java, Spring Boot e Spring Security. Oferece features profissionais como soft delete, roles e permissões, envio de e-mails via SMTP e mais.

---

## 🔧 Tecnologias

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Flyway (migrations)
- Docker & Docker Compose
- Swagger
- SMTP

---

## 🚀 Funcionalidades

- Cadastro de usuários com ativação via e-mail
- Login com JWT
- Soft delete de usuários
- Gerenciamento de roles e permissões
- Redefinição de senha via OTP (envio de código por e-mail)
- Paginação de listagem de usuários (somente Admin)
- CRUD de usuários

---

## 📦 Pré-requisitos

- Docker
- Docker Compose
- Git

---

## ⚙️ Configuração e execução

1. Clone este repositório e navegue até a pasta do projeto:
   ```bash
   git clone https://github.com/pedro-scarelli/LoginSpringSecurity.git
   cd LoginSpringSecurity
   ```

2. Inicie a aplicação com Docker Compose:
   ```bash
   docker compose up --build
   ```

A API estará disponível em `http://localhost:8080`.

---
## 📋 Documentação

Link para o swagger: http://localhost:8080/swagger-ui/index.html

## Endpoints

### 1. Cadastro de usuário

```bash
POST /v1/user
Content-Type: application/json

{
  "name": "{NOME_DO_USUARIO}",
  "email": "{EMAIL_DO_USUARIO}",
  "password": "{SENHA_DO_USUARIO}"
}
```
- Envia e-mail de confirmação para ativação da conta.

### 2. Ativação de conta

```bash
GET /v1/user/activate/{ID_DO_USUARIO}
```

### 3. Login

```bash
POST /v1/auth/login
Content-Type: application/json

{
  "email": "{EMAIL_DO_USUARIO}",
  "password": "{SENHA_DO_USUARIO}"
}
```
- Para contas criadas pelo Flyway, utilize a senha padrão: `senha123`.

### 4. Listar usuários (somente Admin)

```bash
GET /v1/user?page={NÚMERO_PAGINA}&items={QTD_ITENS}
Authorization: Bearer {TOKEN_JWT}
```

### 5. Obter usuário

```bash
GET /v1/user/{ID_DO_USUARIO}
Authorization: Bearer {TOKEN_JWT}
```

### 6. Atualizar usuário

```bash
PATCH /v1/user/{ID_DO_USUARIO}
Content-Type: application/json
Authorization: Bearer {TOKEN_JWT}

{
  "name": "{NOVO_NOME}",
  "password": "{NOVA_SENHA}"
}
```

### 7. Deletar usuário (soft delete)

```bash
DELETE /v1/user/{ID_DO_USUARIO}
Authorization: Bearer {TOKEN_JWT}
```
- Marca o usuário como deletado e o remove das buscas.

### 8. Ativar redefinição de senha

```bash
POST /v1/auth/redefine-password/activate
Content-Type: application/json

{
  "email": "{EMAIL_DO_USUARIO}"
}
```
- Envia e-mail com código OTP.

### 9. Redefinir senha

```bash
PATCH /v1/auth/redefine-password
Content-Type: application/json

{
  "email": "{EMAIL_DO_USUARIO}",
  "otpCode": "{CODIGO_OTP}",
  "newPassword": "{NOVA_SENHA}"
}
```

---

## 📸 Screenshots

![Cadastro de usuário](https://github.com/user-attachments/assets/512f464a-dee8-4265-b9ae-5c91f74d7daa)

![OTP para redefinição de senha](https://github.com/user-attachments/assets/6201f6af-e628-4d66-b8e3-c4344ac99455)

---
