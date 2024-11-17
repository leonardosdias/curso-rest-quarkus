# Quarkus social

Projeto de estudo prático com Quarkus para desenvolvimento de uma API REST que permita simular operações realizadas de uma rede social, tais como, seguir usuários, incluir posts, listar posts entre outros.

## Começando

Essas instruções permitirão que você obtenha uma cópia do projeto em operação na sua máquina local para fins de desenvolvimento e teste.

### 📋 Pré-requisitos

Ferramentas necessárias para executar o projeto

```
- Java 17.0.12
- Maven 3.8.8
- Quarkus 3.8.3 
- Git
- Insomnia
- Postgres SQL
- Docker
```

### Instalação

Uma série de exemplos passo-a-passo que informam o que você deve executar para ter um ambiente de desenvolvimento em execução.

Clonar o projeto localmente::

```
git clone https://github.com/leonardosdias/curso-rest-quarkus.git
```

Acessar o diretório do projeto clonado e instalar as dependências necessárias:

```
mvn clean install
```

Executar o projeto localmente:

```
mvn compile quarkus:dev
```

## Testes unitários

mvn compile quarkus:test

## Construído com

Principais tecnologias usadas nesse projeto:

* [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) - Linguagem de programação
* [Quarkus](https://pt.quarkus.io/) - Framework backend
* [Maven](https://maven.apache.org/) - Gerenciador de dependências Java

## Autores

* **Leonardo Dias** - *Desenvolvimento da aplicação* - [https://github.com/leonardosdias](https://github.com/leonardosdias)


---
Template readme desenvolvido por [Armstrong Lohãns](https://gist.github.com/lohhans)