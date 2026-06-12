# 💻 Sobre o projeto
Reserva de salas 2 é um desafio da Alura, continuação do desafio anterior (reserva de salas 1). O projeto foi dividido em microsserviços, onde cada microsserviço se comunica por sistemas de mensageria (Kafka e RabbitMQ) <br> A aplicação é um sistema para o usuário logar e realizar a reserva de salas com base na disponibilidade de horários.

---

# 🛠 Stack utilizada
As seguintes tecnologias foram utilizadas no desenvolvimento da API Rest do projeto:
* `Java` v.21
* `Spring Boot`
* `Spring Cloud`
* `Spring Data JPA`
* `Spring Security`
* `Eureka Server`
* `Openfeign`
* `Lombok`
* `Kafka`
* `RabbitMQ`
* `MySQL`
* `Flyway`
* `JWT`
* `Docker`

---

# 📓 Funcionalidades

- [x] Login e cadastro de usuários;
- [x] Cadastro de salas;
- [x] Busca de salas;
- [x] Desativação de salas (por administradores);
- [x] Agendamento de reservas de salas;
- [x] Consulta e cancelamento de reservas de salas;
- [x] Consulta de disponibilidade de salas por horário;
- [x] Envio automático de email para confirmação de reserva;

---

# 🔐 Autenticação

A API utiliza JWT.

Fluxo:

* POST /login
* Retorno: token
* Enviar token no header:
  * Authorization: Bearer {token}

---

# 🧱 Arquitetura

O sistema é composto pelos seguintes microsserviços:
- Serviço de Usuários
- Serviço de Salas
- Serviço de Reservas
- Serviço de Log
- Serviço de Email
- Eureka Server
- Gateway

Cada microsserviço possui seu banco de dados próprio. Todos são MySQL.

## 📞 Comunicação

**Comunicação síncrona**: OpenFeign <br>
**Comunicação assíncrona**: <br>
Kafka: Serviço de Reservas → Serviço de log (Reserva feita) <br>
RabbitMQ: Serviço de Reservas → Serviço de email (Aviso de agendamento para email do usuário)

[Cliente] <br>
   ↓ <br>
[Gateway] <br>
   ↓ <br>
[Reservas] <br>
↙       ↘ <br>
[Usuários]  [Salas] <br>
   ↓ <br>
(Kafka / RabbitMQ)

---

# 📨 Mensageria

Tipos de mensagens e e eventos publicados:

**FIla**: reservas.enviar-email <br>
**Publicado no**: RabbitMQ <br>
**Consumidor**: Serviço de Email (via exchange reservas.ex, do tipo Fanout)<br>
**Estrutura do evento:**

```
{
  "usuarioId": 3,
  "inicio": "2026-02-24T14:00:00",
  "fim": "2026-02-24T16:00:00",
  "salaId": 8
}
```

**Tópico**: reserva-topic <br>
**Publicado no**: Kafka <br>
**Consumidor**: Serviço de Log <br>
**Estrutura da mensagem:**
```
{
  "id": 15,
  "usuarioId": 3,
  "sala": 8,
  "inicio": "2026-02-24T14:00:00",
  "fim": "2026-02-24T16:00:00",
  "quantidade": 10,
  "status": "ATIVA"
}
```


---

## ⚙️ Endpoints

A API expõe os seguintes *endpoints* a partir da *base URL* `localhost:8081`:

Microsserviço de usuários:
* `GET /busca-email/{email}`
* `GET /busca-id/{id}`
* `POST /cadastrar`
* `POST /login`

Microsserviço de salas:
* `GET /buscar/{id}`
* `GET /todas`
* `GET /todas/{ids}`
* `POST /cadastrar`
* `PATCH /desativar/{id}`

Microsserviço de reservas:
* `GET /{idReserva}`
* `GET /todas`
* `GET /disponiveis`
* `POST /agendar`
* `PATCH /cancelar/{id}`

---

# ▶ Como executar

Subir dependências:

```docker-compose up -d```

Acessar a URL base do gateway:

``http://localhost:8081``
