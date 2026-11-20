<div align="center">

# 💧 Pluvia

### Monitoramento inteligente de terrenos para prevenção de incêndios

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange?style=flat-square)](https://developer.android.com/topic/architecture)
[![API](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-1565C0?style=flat-square)](https://developer.android.com/about/versions/oreo)

</div>

---

## 📋 Sobre o Projeto

O **Pluvia** é um aplicativo Android nativo desenvolvido como projeto de **Global Solution** da disciplina de Engenharia de Software da **FIAP**, turma **3ESOA** — 2026.

O tema escolhido para esta edição da Global Solution foi a **utilização de Inteligência Artificial para monitorar, prevenir e reduzir incêndios em terrenos rurais**. O Pluvia representa a camada mobile dessa solução, permitindo que produtores rurais acompanhem em tempo real as condições climáticas de seus terrenos, recebam alertas de risco de incêndio e gerenciem sistemas de irrigação inteligente.

Link video pitch:
https://youtu.be/Z_FdS5WcpW0

---

## 🎯 Problema & Solução

### O Problema
Incêndios em terrenos rurais causam prejuízos bilionários ao agronegócio brasileiro e impactos ambientais irreversíveis. A falta de monitoramento contínuo e a demora na detecção de condições de risco são os principais fatores que transformam focos controláveis em grandes incêndios.

### A Solução
O Pluvia integra dados de sensores IoT instalados nos terrenos (temperatura, umidade do ar e pressão atmosférica) com um modelo de IA que calcula em tempo real o **índice de risco de incêndio** de cada propriedade. Com base nesses dados, o sistema pode acionar automaticamente sistemas de irrigação preventiva, notificar o proprietário e sugerir ações corretivas antes que o fogo se inicie.

---

## 👥 Equipe

| Nome | RM |
|------|----|
| [Gabriel Miranda]   | RM 559102 |
| [Camila Feitosa]    | RM 558808 |
| [Gabriel Matiolli]  | RM 558963 |
| [Gustavo Berlanga]  | RM 555298 |
| [Stéffany Medeiros] | RM 556262 |

> **Instituição:** FIAP — Faculdade de Informática e Administração Paulista  
> **Curso:** Engenharia de Software  
> **Turma:** 3ESOA  
> **Ano:** 2026

---

## 📱 Funcionalidades

### Autenticação
- ✅ Login com e-mail e senha
- ✅ Cadastro de nova conta com validação de campos
- ✅ Persistência de sessão entre aberturas do app
- ✅ Logout seguro

### Terrenos
- ✅ Listagem de todos os terrenos cadastrados
- ✅ Cadastro de novo terreno com dados completos de endereço e localização
- ✅ Edição de terrenos existentes
- ✅ Visualização de dados climáticos em tempo real (temperatura, umidade, pressão)
- ✅ Indicador visual de risco de incêndio com barra de progresso colorida
- ✅ Status do sistema de irrigação (ativo/inativo)
- ✅ Data da última irrigação

### Perfil
- ✅ Visualização dos dados do usuário logado
- ✅ Edição de nome e e-mail
- ✅ Avatar com iniciais do nome

### UX / Interface
- ✅ Design system com paleta azul/ciano (tema água — Pluvia)
- ✅ Bottom Navigation Bar com 3 abas (Início, Terreno, Perfil)
- ✅ Suporte a edge-to-edge com respeito à status bar nativa
- ✅ Validação de campos com limites de caracteres e tipos de entrada
- ✅ Feedback visual de loading e erros via Snackbar

---

## 🏗️ Arquitetura

O projeto segue a arquitetura **MVVM (Model-View-ViewModel)** com separação clara de responsabilidades em camadas:

```
app/
└── src/main/java/br/com/pluvia/
    ├── data/
    │   ├── model/          # Data classes (entidades de domínio)
    │   ├── remote/         # Retrofit, MockInterceptor, MockDataStore
    │   └── repository/     # Repositórios (ponte entre ViewModel e API)
    ├── ui/
    │   ├── component/      # Componentes reutilizáveis (AppButton, AppTextField, etc.)
    │   ├── navigation/     # NavGraph e rotas
    │   ├── screen/         # Telas (Login, Register, Home, AddTerrain, EditTerrain, Profile, EditProfile)
    │   ├── theme/          # Cores, tipografia e tema Material3
    │   └── viewmodel/      # ViewModels (AuthViewModel, TerrainViewModel, ProfileViewModel)
    ├── util/               # Classes utilitárias (Resource sealed class)
    ├── PluviaApp.kt        # Application class
    └── MainActivity.kt     # Entry point
```

### Fluxo de dados

```
Screen (Composable)
    ↕ observa StateFlow
ViewModel
    ↕ chama suspend fun
Repository
    ↕ faz requisição HTTP
Retrofit + MockInterceptor
    ↕ intercepta e responde com dados mockados
MockDataStore (SharedPreferences)
```

---

## 🛠️ Tecnologias & Bibliotecas

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Linguagem** | Kotlin | 2.0.0 |
| **UI Framework** | Jetpack Compose | BOM 2024.08.00 |
| **Design System** | Material Design 3 | — |
| **Navegação** | Navigation Compose | 2.7.7 |
| **Arquitetura** | ViewModel + StateFlow | 2.8.4 |
| **Rede** | Retrofit 2 | 2.11.0 |
| **HTTP Client** | OkHttp 3 | 4.12.0 |
| **Serialização** | Gson | 2.11.0 |
| **Coroutines** | Kotlinx Coroutines | 1.8.1 |
| **Persistência** | SharedPreferences (via DataStore) | 1.1.1 |
| **Build System** | Gradle (Kotlin DSL) | 8.5.2 |
| **Min SDK** | Android 8.0 (Oreo) | API 26 |
| **Target SDK** | Android 15 | API 35 |

---

## 🔌 Camada de Dados

O app consome a API real do backend (`pluvia-backend`) via Retrofit, configurada por `API_BASE_URL` no `.env`.

### Usuário administrador pré-cadastrado

O backend cria automaticamente um usuário `Admin` ao aplicar a migration `AddRolesAndSeedAdmin` (`dotnet ef database update`):

| E-mail | Senha | Role |
|--------|-------|------|
| `admin@pluvia.local` | `Admin@123` | `Admin` |

> Troque essa senha antes de qualquer ambiente que não seja local/dev. Para usuários comuns (`User`), não há seed — crie pela tela de cadastro do app (`POST /api/auth/register`).

### Backend (`pluvia-backend`)

O backend também usa um arquivo `.env` (em `Pluvia.WebApi/.env`), com um `.env.example` correspondente contendo `ConnectionStrings__DefaultConnection`, `JWT__Key/Issuer/Audience/ExpiresInMinutes` e `OPENWEATHER__APIKEY/BASEURL`. É necessário uma chave válida da [OpenWeatherMap](https://openweathermap.org/api) em `OPENWEATHER__APIKEY` para os terrenos receberem dados de clima reais — sem ela, cadastro e listagem de terrenos continuam funcionando normalmente, só ficam sem o histórico de previsão.

---

## 🚀 Como executar

### Pré-requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 11+
- Android SDK API 26+
- Dispositivo físico ou emulador com Android 8.0+

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/GabrielMMiranda/pluvia-android.git

# 2. Configure a URL da API
cp .env.example .env
# Edite .env se necessário — o padrão já aponta para o backend local
# rodando em http://127.0.0.1:5081 (via alias de emulador 10.0.2.2)

# 3. Abra no Android Studio
# File > Open > selecione a pasta do projeto

# 4. Aguarde o Gradle sync

# 5. Execute no dispositivo/emulador
# Run > Run 'app'  (ou Shift+F10)
```

> O app consome a API real definida em `API_BASE_URL` (arquivo `.env`, lido em `app/build.gradle.kts` e exposto via `BuildConfig.API_BASE_URL`). É necessário ter o backend (`pluvia-backend`) rodando localmente — use `10.0.2.2` em vez de `127.0.0.1` ao testar em emulador Android, pois `10.0.2.2` é o alias que o emulador usa para acessar o `localhost` da máquina host.

---

## 📐 Padrões de Código

- **Idioma:** Todo o código, variáveis e comentários estão em **inglês**
- **Componentização:** Componentes de UI reutilizáveis isolados em `ui/component/`
- **Estado:** Gerenciado exclusivamente via `StateFlow` nos ViewModels
- **Validação:** Feita na camada ViewModel, não nos composables
- **Navegação:** Centralizada no `AppNavGraph` com rotas tipadas
- **Sem comentários no código:** O código é autoexplicativo por nomenclatura

---

## 🎨 Design System

A paleta de cores foi escolhida para remeter à água e à irrigação, identidade central do Pluvia:

| Token | Cor | Uso |
|-------|-----|-----|
| `BluePrimary` | `#1565C0` | Botões, seleção ativa, títulos |
| `BlueBackground` | `#F0F6FF` | Fundo das telas |
| `BlueSurface` | `#E3EEFF` | Cards, chips de clima, superfícies |
| `CyanGradientStart` | `#29B6F6` | Início do gradiente do logo |
| `CyanGradientEnd` | `#1565C0` | Fim do gradiente do logo |
| `OrangeRisk` | `#FF9800` | Indicador de médio risco |
| `RedLogout` | `#E53935` | Botão de sair / alto risco |

---

## 📄 Licença

Este projeto foi desenvolvido exclusivamente para fins acadêmicos como parte da **Global Solution 2026** da FIAP. Todos os direitos reservados aos autores.

---

<div align="center">

Desenvolvido com 💧 por alunos da **FIAP** — Turma 3ESOA · Global Solution 2026

</div>
