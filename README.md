# 🎲 Jogo de Dados — Spring Boot + Firestore

Jogo de dados com leaderboard em tempo real. Backend Java/Spring Boot, banco de dados Firestore (Firebase).

## Pré-requisitos

- Java 17+
- Maven 3.8+
- Conta no [Firebase](https://firebase.google.com)

---

## ⚙️ Configuração do Firebase

### 1. Criar o projeto no Firebase

1. Acesse [console.firebase.google.com](https://console.firebase.google.com)
2. Clique em **Adicionar projeto** e siga os passos
3. No menu lateral, vá em **Firestore Database** → **Criar banco de dados**
4. Escolha o modo **Produção** ou **Teste** (teste é mais fácil para começar)

### 2. Gerar a chave de serviço (Service Account)

1. Vá em **Configurações do projeto** (ícone de engrenagem) → **Contas de serviço**
2. Clique em **Gerar nova chave privada**
3. Baixe o arquivo `.json` gerado
4. Renomeie para `serviceAccountKey.json` e coloque na raiz do projeto

> ⚠️ **NUNCA envie esse arquivo para o GitHub.** Ele já está no `.gitignore`.

### 3. Definir a variável de ambiente

**Linux / Mac:**
```bash
export FIREBASE_CREDENTIALS_PATH=/caminho/para/serviceAccountKey.json
```

**Windows (Prompt):**
```cmd
set FIREBASE_CREDENTIALS_PATH=C:\caminho\para\serviceAccountKey.json
```

**Windows (PowerShell):**
```powershell
$env:FIREBASE_CREDENTIALS_PATH="C:\caminho\para\serviceAccountKey.json"
```

---

## ▶️ Rodando o projeto

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/dice-game.git
cd dice-game

# 2. Defina a variável de ambiente (veja acima)

# 3. Execute
mvn spring-boot:run
```

Acesse: [http://localhost:8080](http://localhost:8080)

---

## 🌐 Endpoints da API

| Método | Rota              | Descrição                    |
|--------|-------------------|------------------------------|
| POST   | `/api/game/play`  | Processa uma jogada          |
| GET    | `/api/leaderboard`| Retorna o top 10 jogadores   |

**Exemplo de requisição (POST /api/game/play):**
```json
{
  "playerName": "Miguel",
  "die1": 4,
  "die2": 3
}
```

**Resposta:**
```json
{
  "die1": 4,
  "die2": 3,
  "sum": 7,
  "target": 9,
  "won": false,
  "totalWins": 1,
  "totalLosses": 3,
  "winRate": 25.0
}
```

---

## 🔐 Segurança das credenciais

| Arquivo                        | Vai ao GitHub? |
|-------------------------------|----------------|
| `serviceAccountKey.json`      | ❌ Nunca       |
| `serviceAccountKey.example.json` | ✅ Sim (sem dados reais) |
| `application.properties`      | ✅ Sim (usa variável de ambiente) |
| `.env`                        | ❌ Nunca       |
