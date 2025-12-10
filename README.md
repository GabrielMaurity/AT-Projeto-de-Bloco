# Sistema de Gestão de Produtos e Fornecedores (Enterprise CRUD)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-green)
![Selenium](https://img.shields.io/badge/Selenium-Testing-blue)

## 🏗️ Arquitetura do Sistema
O sistema foi desenvolvido seguindo os princípios de **Clean Code** e **Arquitetura em Camadas**:
- **Controller:** Interface Web (Thymeleaf) e Endpoints REST.
- **Service:** Regras de negócio, Integração entre domínios e Tratamento de Falhas (Fail Gracefully).
- **Model:** Entidades imutáveis (`Records`) com validação robusta (`Fail Early`).
- **Exception:** Tratamento global de erros centralizado.

## 🚀 Pipeline de CI/CD (GitHub Actions)
O projeto conta com uma esteira automatizada dividida em 4 estágios:

1.  **Build & Unit Test:** Compilação com Maven e execução de testes unitários/integração.
2.  **Security Analysis:** Verificação automatizada de dependências vulneráveis (OWASP).
3.  **Staging & Smoke Test:** Deploy simulado e execução de testes E2E (Selenium) pós-deploy para validar a integridade da interface.
4.  **Production Deploy:** Geração automática de Release no GitHub com o artefato versionado.

## 🧪 Estratégias de Testes
- **Testes de Propriedade (Jqwik):** Fuzzing testing para garantir robustez contra dados aleatórios.
- **Testes E2E (Selenium):** Validação dos fluxos críticos (Cadastro, Edição, Exclusão) usando Page Object Model.
- **Testes de Caos:** Simulação de latência e timeouts no banco de dados.

## 📝 Como Executar
1. Clone o repositório.
2. Execute `mvn spring-boot:run`.
3. Acesse `http://localhost:8080`.
