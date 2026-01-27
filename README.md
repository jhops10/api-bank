# 🏦 API Bank - Sistema Bancário

API RESTful para operações bancárias, desenvolvida em **Java com Spring Boot**.

---

## 📋 Visão Geral

**API Bank** é uma aplicação backend que simula operações bancárias básicas, permitindo:

* Transações entre carteiras
* Consulta de saldo
* Emissão de extratos com paginação
* Gerenciamento de carteiras

---

## 🚀 Tecnologias

* **Java 17**
* **Spring Boot**
* **Spring Data JPA**
* **Spring Validation**
* **Maven**
* **Docker**

---

## 🏗️ Estrutura do Projeto

```text
api-bank/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── apibank/
│       │           ├── controllers/
│       │           ├── services/
│       │           ├── repositories/
│       │           ├── models/
│       │           └── config/
│       └── resources/
│           └── application.properties
├── docker/
├── .mvn/wrapper/
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

## ✨ Funcionalidades

### 👛 Gestão de Contas (Wallets)

Funcionalidades relacionadas ao gerenciamento de carteiras digitais (wallets), responsáveis por armazenar saldo e registrar movimentações financeiras.

### Funcionalidades disponíveis

- Criação de Carteira (Wallet) com validação de dados do titular:
  - Nome obrigatório
  - CPF válido
  - E-mail válido
- Exclusão de Carteira por id (UUID)
  - Retorno adequado quando a carteira não é encontrada
- Depósito de valores
  - Validação de valor mínimo permitido
  - Registro do endereço IP de origem da operação
- Consulta de extrato (statements):
  - Listagem paginada de movimentações financeiras
  - Suporte a parâmetros de página e tamanho da página
  - Retorno estruturado em DTO específico para extrato

### Regras de negócio aplicadas

- Operações só são permitidas para carteiras existentes
- Depósitos atualizam o saldo de forma transacional
- Todas as movimentações financeiras são persistidas para composição do extrato
- Paginação aplicada para evitar consultas não performáticas

---

### 🔄 Transferências

Funcionalidades responsáveis pela movimentação de valores entre carteiras.

### Funcionalidades disponíveis

- Transferência de saldo entre duas wallets
- Validação completa da requisição de transferência
- Operação executada de forma transacional

### Regras de negócio aplicadas

- Verificação da existência das carteiras de origem e destino
- Validação de saldo suficiente antes da transferência
- Garantia de consistência entre débito e crédito
- Prevenção de estados inconsistentes em caso de falha

---

### 📄 Extrato (Statements)

Funcionalidade responsável pela consulta do histórico financeiro de uma carteira.

- Listagem de todas as movimentações da carteira
- Paginação configurável via query parameters
- Organização cronológica das operações

---

### ⚙️ Tratamento de Erros e Validações

- Validação automática de dados de entrada via Bean Validation
- Tratamento centralizado de exceções com `@RestControllerAdvice`
- Respostas de erro padronizadas utilizando `ProblemDetail`
- Retorno estruturado de erros de validação contendo:
  - Campo inválido
  - Motivo do erro
  - Mensagem descritiva

---

### 🛠️ Pré-requisitos

* **Java 17** ou superior
* **Maven 3.8+**
* **Docker** (opcional)

---

## 🔧 Instalação e Execução

### ▶️ Usando Maven

```bash
# Clone o repositório
git clone https://github.com/jhops10/api-bank.git
cd api-bank

# Compile o projeto
mvn clean compile

# Execute a aplicação
mvn spring-boot:run
```

### 🐳 Usando Docker

```bash
# Build da imagem Docker
docker build -t api-bank .

# Executar container
docker run -p 8080:8080 api-bank
```

---

## 📦 Build

```bash
# Criar arquivo JAR executável
mvn clean package
```

O arquivo será gerado em:

```
target/api-bank-0.0.1-SNAPSHOT.jar
```

---

📅 **Última atualização:** Janeiro 2026
