# ServeRest-Actions
[![ServeRest Test Pipeline](https://github.com/CaioVasconcellos/ServeRest-Actions/actions/workflows/main.yml/badge.svg)](https://github.com/CaioVasconcellos/ServeRest-Actions/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CaioVasconcellos_ServeRest-Actions&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=CaioVasconcellos_ServeRest-Actions) 
[![Code Review GPT](https://github.com/CaioVasconcellos/ServeRest-Actions/actions/workflows/review.yml/badge.svg)](https://github.com/CaioVasconcellos/ServeRest-Actions/actions/workflows/review.yml)


## Descrição
Este repositório contém um projeto de testes automatizados utilizando **RestAssured** para validar a API do **ServeRest**. Além da implementação dos testes, também foi configurada uma **pipeline de CI/CD** com **GitHub Actions** para execução automatizada dos testes.

## Tecnologias Utilizadas
- **Java 17**
- **RestAssured** (para testes de API)
- **JUnit** (framework de testes)
- **Maven** (gerenciamento de dependências)
- **GitHub Actions** (CI/CD)
- **Allure Reports** (relatórios de execução)
- **CodeQL** (Análise Estática de código)
- **Sonarqube** (Análise contínua da qualidade do código, verificando vulnerabilidades, bugs e padrões de código)

<div style="display:flex; gap:10px;">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" width="40" alt="java logo"  />
  <img src="https://avatars.githubusercontent.com/u/19369327?s=200&v=4" height="40" width="40" alt="restaAssured logo"  />
  <img src="https://junit.org/junit5/assets/img/junit5-logo.png" height="40" width="40" alt="junit logo"  />
  <img src="https://www.svgrepo.com/show/373829/maven.svg" height="40" width="40" alt="Maven logo"  />
  <img src="https://avatars.githubusercontent.com/u/44036562?s=200&v=4" height="40" width="40 alt="Github Actions logo"  />
  <img src="https://avatars.githubusercontent.com/u/5879127?s=48&v=4" height="40" width="40" alt="allure report logo"  />
  <img src="https://www.svgrepo.com/show/373515/codeql.svg" height="40" width="40" alt="codeql logo"  />
  <img src="https://media.discordapp.net/attachments/1341477241929859165/1342143792349843477/sonarcloud.1024x896.png?ex=67b8904f&is=67b73ecf&hm=467060532e1e2ed6cd532aa3f045fd4e353a42a97fefed7cbccfe0bcd3027750&=&format=webp&quality=lossless&width=643&height=563" height="40" width="40" alt="sonar logo"/>

</div>

## Estrutura do Projeto
```
ServeRest-Actions/
│-- .github/workflows/
│   ├── main.yml  # Configuração do CI/CD com GitHub Actions
│-- src/
│   ├── main/java/com.vemser.rest/
│   │   ├── client/  # Clientes HTTP para interagir com a API
│   │   ├── data/    # Dados utilizados nos testes
│   │   ├── model/   # Modelos de dados da API
│   │   ├── utils/   # Utilitários auxiliares
│   ├── test/java/com.vemser.rest.tests/
│   │   ├── healthCheck/  # Testes de saúde da API
│   │   ├── login/        # Testes de login
│   │   ├── produto/      # Testes de produtos (GET, POST, PUT, DELETE)
│   │   ├── usuario/      # Testes de usuários (GET, POST, PUT, DELETE)
│-- pom.xml  # Configuração do Maven
│-- README.md  # Documentação do projeto
```

## Configuração e Execução dos Testes
1. Clone o repositório:
   ```sh
   git clone https://github.com/CaioVasconcellos/ServeRest-Actions.git
   ```
2. Acesse o diretório do projeto:
   ```sh
   cd ServeRest-Actions
   ```
3. Execute os testes com Maven:
   ```sh
   mvn clean test
   ```
4. Para gerar o relatório do **Allure**:
   ```sh
   mvn allure:serve
   ```


## Integração com GitHub Actions
O repositório conta com uma **pipeline de CI/CD** configurada no GitHub Actions. A cada  *push* e *pull request* para a branch `main`, a pipeline é executada automaticamente.


### 1. **Build**
- **Descrição**: Compila o projeto sem rodar testes.
- **Passos**:
    1. Faz o checkout do repositório.
    2. Configura o JDK 17.
    3. Executa `mvn clean install -DskipTests` para construir o projeto.
    4. Armazena o *build* como artefato para uso posterior.

### 2. **Health Check**
- **Descrição**: Executa testes básicos para verificar a saúde da aplicação.
- **Passos**:
    1. Faz o checkout do repositório.
    2. Baixa o *build* da etapa anterior.
    3. Executa testes da classe `HealthCheckTest`.
    4. Armazena o resultado como artefato.

### 3. **Testes de Contrato**
- **Descrição**: Testa a conformidade das respostas da API com os *schemas* definidos.
- **Passos**:
    1. Faz o checkout do repositório.
    2. Baixa o artefato da etapa anterior.
    3. Executa testes do grupo `schema`.
    4. Armazena os resultados.

### 4. **Testes Funcionais**
- **Descrição**: Testa o funcionamento da API ignorando os testes de contrato.
- **Passos**:
    1. Faz o checkout do repositório.
    2. Baixa o artefato da etapa anterior.
    3. Executa testes excluindo o grupo `schema`.
    4. Armazena os resultados.

### 5. **Geração de Relatório Allure**
- **Descrição**: Gera e publica um relatório de testes no GitHub Pages.
- **Passos**:
    1. Faz o checkout do repositório.
    2. Baixa os artefatos das etapas anteriores.
    3. Executa `mvn allure:report` para gerar o relatório.
    4. Publica no GitHub Pages.

### 6. **Análise de Código com CodeQL**
- **Descrição**: Executa análise estática de segurança com CodeQL.
- **Passos**:
    1. Faz o checkout do repositório.
    2. Inicializa o CodeQL.
    3. Compila o código automaticamente.
    4. Executa a análise.

### 7. **Análise com SonarQube**
- **Descrição**: Avalia a qualidade do código e reporta ao SonarCloud.
- **Passos**:
    1. Faz o checkout do código.
    2. Executa testes sem interromper a pipeline.
    3. Envia os resultados ao SonarCloud.

### 8. **Notificação no Discord**
- **Descrição**: Envia um resumo da execução para um canal do Discord.
- **Passos**:
    1. Monta uma mensagem JSON com os resultados das etapas.
    2. Envia via webhook para um canal no Discord.

## Estrutura da Pipeline
### Sucesso
![image](https://github.com/user-attachments/assets/30d81d77-a59e-41bd-a32f-9da2362addcf)
|:---:|
### Falha
![image](https://github.com/user-attachments/assets/fbc02295-c771-4e88-9630-c7dea7aaf5ec)
|:---:|
## Integração com o Discord
![image](https://github.com/user-attachments/assets/7dd05255-dfc2-46c7-89f7-c758dfffb4bb)
|:---:|

## Resultado do Sonarqube
![image](https://github.com/user-attachments/assets/470a49ee-8e05-47f6-be70-b2d767de069a)
|:---:|

## Resultado Allure Pages
![image](https://github.com/user-attachments/assets/39e40dd8-7614-411a-af98-15d6d4332a91)
|:---:|

## Autor
Desenvolvido por **Héctor Tavares** e **Caio Vasconcellos**, como parte dos estudos em testes automatizados e integração com GitHub Actions.










