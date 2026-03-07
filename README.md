# Build Pipeline Refactoring Kata - TP2

Este repositório contém a refatoração de um sistema legado de Pipeline de Build, realizada como parte do Trabalho Prático 2 (TP2) da disciplina de Engenharia de Software (Refactoring) em 2026.

## 🛠️ Projeto Original
O sistema original apresentava um método `run` na classe `Pipeline` com alta complexidade ciclomática, múltiplas responsabilidades (violação do SRP) e uso extensivo de "Magic Strings" e estruturas de decisão aninhadas (`if/else`).

## 🚀 Melhorias Realizadas

### 1. Rede de Segurança (Testes)
Antes de qualquer alteração, foi implementada uma suíte de testes unitários utilizando **JUnit 5** e **Mockito**. Os testes cobrem todos os fluxos: sucesso total, falha nos testes, falha no deploy e desativação de notificações.

### 2. Decomposição de Métodos (Extract Method)
O método principal foi quebrado em métodos privados menores e especialistas: `executeTests`, `executeDeployment` e `sendNotification`. Isso melhorou a legibilidade e permitiu o uso de **Guard Clauses** (retornos antecipados) para eliminar o aninhamento de código.

### 3. Expressividade com Constantes
Todas as strings literais foram extraídas para constantes e variáveis explicativas. Isso centralizou a manutenção de textos e status do sistema, eliminando redundâncias.

### 4. Encapsulamento de Estado (Java Records)
Foi introduzido um `record PipelineStatus` para agrupar os resultados do processo de forma imutável, simplificando a assinatura dos métodos e protegendo os dados durante o fluxo.

### 5. Separação de Responsabilidades (SRP)
A lógica de geração de mensagens foi movida para uma classe dedicada, a `PipelineMessenger`, permitindo que a classe `Pipeline` foque exclusivamente na orquestração do processo.

## ⚙️ Tecnologias
* Java 25 (ou 21 conforme compatibilidade do ambiente)
* Maven
* JUnit 5
* Mockito
* GitHub Actions (CI)
