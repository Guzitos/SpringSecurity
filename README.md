# 🛡️ Spring Security + JWT + Docker + MySQL

Projeto backend desenvolvido com **Spring Boot**, utilizando **Spring Security**, **JWT (OAuth2 Resource Server)**, **JPA/Hibernate**, **MySQL em Docker** e **controle de permissões por roles**.

Este projeto foi construído com foco **educacional**, ideal para estudos e também para demonstrar conhecimentos em entrevistas de nível **estudante / júnior**.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security
- JWT (OAuth2 Resource Server)
- Spring Data JPA / Hibernate
- MySQL 8 (Docker)
- Docker Compose
- Lombok

---

## 🧠 Conceitos Aplicados

- Autenticação stateless com JWT
- Autorização baseada em roles (ADMIN / BASIC)
- Proteção de rotas com Spring Security
- Uso de `@PreAuthorize`
- Relacionamentos JPA (`@ManyToMany`, `@ManyToOne`)
- Paginação com Spring Data
- Inicialização de dados com `CommandLineRunner`

---

## 📂 Estrutura do Projeto

```
src/main/java/com/loop/springsecurity
│
├── config
│   ├── SecurityConfig.java
│   └── AdminUserConfig.java
│
├── controller
│   ├── TokenController.java
│   ├── UserController.java
│   └── TweetController.java
│
├── controller/dto
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── CreateUserDto.java
│   ├── CreateTweetDto.java
│   ├── FeedDto.java
│   └── FeedItemDto.java
│
├── entities
│   ├── User.java
│   ├── Role.java
│   └── Tweet.java
│
├── repository
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   └── TweetRepository.java
```

---

## 🔐 Segurança (Spring Security + JWT)

- A aplicação **não usa sessão** (`STATELESS`)
- Autenticação é feita via **JWT assinado com RSA**
- O token contém:
  - `subject` → ID do usuário
  - `scope` → roles do usuário (ex: `ADMIN`, `BASIC`)

### Regras de acesso

| Endpoint | Método | Acesso |
|--------|-------|-------|
| `/users` | POST | Público |
| `/login` | POST | Público |
| `/feed` | GET | Autenticado |
| `/tweets` | POST | Autenticado |
| `/tweets/{id}` | DELETE | Dono do tweet ou ADMIN |
| `/users` | GET | Apenas ADMIN |

---

## 👤 Usuário Admin Padrão

Ao iniciar a aplicação, um usuário administrador é criado automaticamente:

```text
username: admin
password: 123
```

Isso é feito através da classe `AdminUserConfig`.

---

## 🐳 Docker + MySQL

O banco de dados roda em um container Docker.

### Subir o banco

```bash
docker-compose up -d
```

### Configuração

- MySQL 8
- Porta: `3307`
- Database: `mydb`
- Usuário: `admin`
- Senha: `123`

Os dados são persistidos usando **volumes Docker**.

---

## 🔄 Fluxo da Aplicação

1. Usuário se cadastra (`POST /users`)
2. Usuário faz login (`POST /login`)
3. Backend gera um **JWT**
4. Cliente envia o token no header:

```
Authorization: Bearer <token>
```

5. Spring Security valida o token
6. Controller recebe o usuário autenticado
7. Autorização é feita por role ou dono do recurso

---

## 📌 Exemplos de Requisições

### Login

```http
POST /login
Content-Type: application/json

{
  "username": "admin",
  "password": "123"
}
```

### Criar Tweet

```http
POST /tweets
Authorization: Bearer <token>
Content-Type: application/json

{
  "content": "Meu primeiro tweet"
}
```

---

## 🎯 Objetivo do Projeto

Este projeto demonstra conhecimento em:

- Backend com Spring Boot
- Segurança com JWT
- Boas práticas REST
- Integração com banco de dados
- Uso de Docker no desenvolvimento

Ideal para:
- Estudo
- Portfólio
- Demonstração técnica em entrevistas

---

## 👨‍💻 Autor

Desenvolvido por **Gustavo** 🚀

---

Se quiser, fique à vontade para clonar, estudar e evoluir este projeto.

