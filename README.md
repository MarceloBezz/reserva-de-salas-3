# 🏢 Reserva de Salas

Este repositório contém uma aplicação de reservas de salas construída com uma arquitetura de microsserviços, com foco em disponibilidade, comunicação assíncrona, automação e deploy containerizado.

A proposta do projeto é permitir que usuários façam login, cadastrem e consultem salas, realizem reservas com base em disponibilidade, recebam notificações e tenham um fluxo completo de observabilidade e integração contínua.

---

## 📖 Visão geral

O sistema foi evoluído para incluir não apenas os serviços principais de usuários, salas e reservas, mas também:

- 💡 um serviço de sugestões de horários;
- ⚙️ um serviço de processamento em lote (batch);
- 📨 integração com mensageria assíncrona via Kafka e RabbitMQ;
- 🔍 observabilidade com Jaeger;
- 🚀 pipeline de CI/CD com GitHub Actions;
- ☸️ deploy com Docker Compose e manifests Kubernetes.

---

## 🏗️ Arquitetura

A aplicação é composta por microsserviços que se comunicam de forma síncrona e assíncrona:

```text
[Cliente / Frontend]
          |
          v
      [Gateway]
          |
          v
    [Serviço de Reservas]
    /        |        \
   /         |         \
  v          v          v
[Usuários] [Salas] [Sugestões]
   |          |          |
   |          |          |
   v          v          v
 [Email]    [Log]    [Batch]

Comunicação síncrona: OpenFeign
Comunicação assíncrona: Kafka e RabbitMQ
```

### 🧩 Componentes principais

- Gateway: entrada única para as requisições externas.
- Server: serviço de descoberta/infraestrutura.
- Usuários: autenticação, cadastro e consulta de perfis.
- Salas: cadastro, busca e gestão de salas.
- Reservas: criação, consulta, cancelamento e disponibilidade.
- Email: envio de notificações para usuários.
- Log: registro/eventos de reservas.
- Batch: processamento e geração de relatórios.
- Sugestões: serviço adicional para sugestão de horários.

---

## 🛠️ Stack utilizada

- Java 21
- Spring Boot
- Spring Cloud
- Spring Security
- Spring Data JPA
- OpenFeign
- JWT
- Kafka
- RabbitMQ
- Redis
- MySQL
- Flyway
- Docker / Docker Compose
- Kubernetes
- Quarkus (serviço de sugestões)
- GitHub Actions
- Jaeger

---

## ✅ Funcionalidades principais

- [x] 👤 Cadastro e login de usuários
- [x] 🏢 Cadastro, consulta e desativação de salas
- [x] 📅 Agendamento de reservas
- [x] 🕒 Consulta de disponibilidade por horário
- [x] ❌ Cancelamento de reservas
- [x] 📧 Envio automático de e-mails de confirmação/notificação
- [x] 📨 Publicação de eventos para log e processamento assíncrono
- [x] 📊 Processamento em lote com geração de relatórios
- [x] 💡 Serviço dedicado para sugestão de horários
- [x] 🚀 Pipeline de CI/CD e deploy containerizado

---

## 📨 Mensageria

O projeto utiliza eventos para integrar os serviços de forma desacoplada.

### 🐇 RabbitMQ

- **Fila:** `reservas.enviar-email`
- **Consumidor:** serviço de e-mail
- **Objetivo:** notificar o usuário após o agendamento de uma reserva.

### 📨 Kafka

- **Tópico:** `reserva-topic`
- **Consumidor:** serviço de log
- **Objetivo:** registrar eventos de reservas de forma assíncrona.

---

## ▶️ Como executar localmente

### 📋 Pré-requisitos

- Docker e Docker Compose
- Java 21
- Maven ou Maven Wrapper

### 1️⃣ Subir os serviços de infraestrutura

```bash
docker compose up -d
```

Essa configuração sobe:

- MySQL
- RabbitMQ
- Kafka
- Redis
- Jaeger
- Microsserviços principais

### 2️⃣ Acessar a aplicação

- Gateway: http://localhost:8081
- RabbitMQ UI: http://localhost:15672
- Jaeger UI: http://localhost:16686

> ℹ️ Os containers utilizam arquivos de ambiente em pastas específicas do projeto. Verifique se os arquivos necessários estão disponíveis antes de subir a stack.

### 3️⃣ Executar um serviço individualmente

Cada módulo conta com o wrapper Maven para execução local:

```bash
./mvnw spring-boot:run
```

ou, no caso do serviço de sugestões:

```bash
./mvnw quarkus:dev
```

---

## 📂 Estrutura do repositório

- `server`: infraestrutura e descoberta
- `gateway`: ponto de entrada da API
- `servico-usuario`: autenticação e usuários
- `servico-sala`: gestão de salas
- `servico-reserva`: reservas e disponibilidade
- `servico-email`: notificações por e-mail
- `servico-log`: eventos e auditoria
- `batch`: processamento em lote
- `servico-sugestao-horarios`: sugestão de horários
- `k8s`: manifests para deploy em Kubernetes
- `.github/workflows`: pipeline de CI/CD

---

## 🚀 CI/CD e deploy

O projeto conta com uma pipeline de integração e entrega contínua configurada no GitHub Actions, que:

- 🔍 detecta alterações por serviço;
- ✅ executa testes quando necessário;
- 🐳 monta e publica imagens Docker para os módulos alterados.

Também há manifests em Kubernetes para implantação dos serviços em ambientes mais robustos.

---

## 📝 Observações

Este README foi atualizado para refletir o estado atual do projeto, incluindo as evoluções de arquitetura, integração assíncrona, processamento em lote, sugestões de horários e infraestrutura de deploy.