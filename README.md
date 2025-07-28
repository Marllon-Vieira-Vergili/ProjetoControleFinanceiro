# Projeto Controle Financeiro

Projeto focado em um CRUD completo com TESTES UNITÁRIOS, realizado para prática isolada após o curso.

## Sobre

Este projeto tem como objetivo praticar a implementação de um sistema de controle financeiro com todas as operações CRUD e a aplicação de testes unitários utilizando JUnit e Mockito.

Além disso, o código foi aprimorado com boas práticas adquiridas durante o desenvolvimento, como por exemplo, a implementação do uso de DTOs para separar a camada de apresentação da lógica do domínio.

## Funcionalidades

- CRUD completo para gerenciamento financeiro  
- Testes unitários com JUnit e Mockito para garantir qualidade do código  
- Uso de DTOs para melhor organização e segurança dos dados  
- Banco de dados em memória H2 para facilitar o desenvolvimento e testes  
- Validações e tratamento de exceções

## Tecnologias

- Java 11+  
- Spring Boot  
- Spring Data JPA  
- H2 Database  
- JUnit  
- Mockito

## Como executar

1. Rode o projeto com `./mvnw spring-boot:run` ou pela IDE de sua preferência.  
2. O banco H2 será iniciado em memória automaticamente (sem configuração externa).  
3. Utilize clientes HTTP para testar os endpoints REST.  
4. Para rodar os testes, execute `./mvnw test` ou use sua IDE.

## Autor

Marllon Vieira Vergili  
[GitHub](https://github.com/Marllon-Vieira-Vergili)

---

Projeto para consolidar conhecimentos em CRUD completo com foco em TESTES UNITÁRIOS e boas práticas como uso de DTOs e melhores separações de responsabilidade nos métodos.
