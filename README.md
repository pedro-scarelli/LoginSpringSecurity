User

# Aplicativo de Gerenciamento de Usuários

Aplicativo web de gerenciamento de usuários desenvolvido usando Spring Boot, Spring Security e Docker.

## Instruções de Configuração

### Pré-requisitos

- Docker
- Docker Compose
- Git

### Inicialização do Servidor

1. No terminal navegue até a pasta onde deseja clonar o projeto e execute os comandos
```bash
git clone https://github.com/pvscarelli/LoginComSpringSecurity.git
cd LoginComSpringSecurity
```
3. Já na pasta root do projeto, execute o comando

```bash
docker-compose up
```
ou caso esteja em uma versão mais recente do docker
```bash
docker compose up
```
para iniciar a api.

4. Se a porta 8080 não estiver disponível ele vai iniciar em outra porta e você devera mudar a porta depois do localhost: nas rotas.

## Documentação da API

### Registro de Usuário

```bash
curl -X POST "http://localhost:8080/v1/users/register" -H 'Content-Type: application/json' -d '{
"name": "exampleUser",
"mail": "user@example.com",
"login": "exampleUser",
"password": "password123" }' | jq '.'
```

### Login de Usuário e Obtenção do token
```bash
token=$(curl -X POST "http://localhost:8080/v1/users/login" -H 'Content-Type: application/json' -d '{
"login": "exampleUser",
"password": "password123" }' | jq -r '.token')
```
#### Caso queira logar em algum dos users criados pelo flyway a senha é: senha123

### Obter Todos os Usuários

- O método obter todos usuários funciona com paginação, substitua o 4 pelo número da página desejado e 2 pela quantidade de itens desejados.
```bash
curl -X GET "localhost:8080/v1/users?page=4&items=2" -H "Authorization: Bearer $token" | jq '.'
```
### Obter Usuário
```bash
curl -X GET "http://localhost:8080/v1/users/idUsuarioAqui" -H "Authorization: Bearer $token" | jq '.'
```
### Atualizar Usuário
```bash
curl -X PUT "http://localhost:8080/v1/users/idUsuariosAqui" -H 'Content-Type: application/json' -H "Authorization: Bearer $token" -d '{ "name": "exampleUserUpdated",
"mail": "update@example.com",
"login": "exampleUpdate",
"password": "newPassword" }'
```
- Se você atualizar o usuário com que fez login precisara fazer o login denovo.

### Excluir Usuário
```bash
curl -X DELETE "http://localhost:8080/v1/users/idUsuariosAqui" -H "Authorization: Bearer $token"
```
### Excluir Todos os Usuários
```bash
curl -X DELETE "http://localhost:8080/v1/users/delete_all" -H "Authorization: Bearer $token"
```
- Depois de excluir todos os usuários o token atual não funcionará mais
- Será necessário cadastrar outro usuário e usar o novo token para verificar que os usuários anteriores foram deletados
