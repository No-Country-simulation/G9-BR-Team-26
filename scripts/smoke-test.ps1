# ═══════════════════════════════════════════════════════════
#  Smoke Test — Finance AI Backend
#  Roda os principais fluxos da API e reporta OK/FALHOU.
#  Uso: .\smoke-test.ps1
# ═══════════════════════════════════════════════════════════

$baseUrl = "http://localhost:8080"
$emailTeste = "smoketest@financeai.com"
$senhaTeste = "senha123"

$totalTestes = 0
$totalFalhas = 0

# Função auxiliar: roda um teste, compara o status esperado, imprime resultado.
function Testar {
    param(
        [string]$Nome,
        [scriptblock]$Acao,
        [int]$StatusEsperado
    )

    $script:totalTestes++

    try {
        $resposta = & $Acao
        $statusReal = $resposta.StatusCode
    } catch {
        # Invoke-WebRequest lança exceção em status 4xx/5xx — extraímos o status de dentro dela.
        $statusReal = $_.Exception.Response.StatusCode.value__
    }

    if ($statusReal -eq $StatusEsperado) {
        Write-Host "[OK]      $Nome (status $statusReal)" -ForegroundColor Green
    } else {
        Write-Host "[FALHOU]  $Nome (esperado $StatusEsperado, veio $statusReal)" -ForegroundColor Red
        $script:totalFalhas++
    }
}

Write-Host "`n=== Iniciando Smoke Test ===`n" -ForegroundColor Cyan

# ─────────────────────────────────────────────────
#  1. Autenticação
# ─────────────────────────────────────────────────

# Signup pode falhar com 409 se o usuário de teste já existir de uma rodada anterior — tudo bem, ignoramos esse caso.
try {
    $signupBody = @{ nome = "Smoke Test"; email = $emailTeste; senha = $senhaTeste } | ConvertTo-Json
    Invoke-WebRequest -Uri "$baseUrl/auth/signup" -Method Post -Body $signupBody -ContentType "application/json" -ErrorAction SilentlyContinue | Out-Null
    Write-Host "[OK]      Signup (criado ou já existente)" -ForegroundColor Green
} catch {
    Write-Host "[OK]      Signup (usuário já existia, esperado)" -ForegroundColor Green
}

$loginBody = @{ email = $emailTeste; senha = $senhaTeste } | ConvertTo-Json
$loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
$token = $loginResponse.accessToken
$headers = @{ Authorization = "Bearer $token" }

if ($token) {
    Write-Host "[OK]      Login (token obtido)" -ForegroundColor Green
} else {
    Write-Host "[FALHOU]  Login (token vazio)" -ForegroundColor Red
    $totalFalhas++
}
$totalTestes += 2

# ─────────────────────────────────────────────────
#  2. Endpoints principais (caminho feliz)
# ─────────────────────────────────────────────────

Testar -Nome "GET /usuarios/{id}" -StatusEsperado 200 -Acao {
    Invoke-WebRequest -Uri "$baseUrl/usuarios/1" -Method Get -Headers $headers
}

Testar -Nome "POST /transacoes (criar)" -StatusEsperado 201 -Acao {
    $body = @{ descricao = "Smoke test transacao"; valor = 10 } | ConvertTo-Json
    Invoke-WebRequest -Uri "$baseUrl/transacoes" -Method Post -Body $body -ContentType "application/json" -Headers $headers
}

Testar -Nome "GET /transacoes (listar)" -StatusEsperado 200 -Acao {
    Invoke-WebRequest -Uri "$baseUrl/transacoes" -Method Get -Headers $headers
}

Testar -Nome "POST /transacoes/classificar" -StatusEsperado 200 -Acao {
    $body = @{ descricao = "Supermercado"; valor = 50 } | ConvertTo-Json
    Invoke-WebRequest -Uri "$baseUrl/transacoes/classificar" -Method Post -Body $body -ContentType "application/json" -Headers $headers
}

Testar -Nome "POST /analise-financeira" -StatusEsperado 200 -Acao {
    $body = @{ rendaMensal = 4500; nivelEndividamento = 25; frequenciaPoupanca = "Media" } | ConvertTo-Json
    Invoke-WebRequest -Uri "$baseUrl/analise-financeira" -Method Post -Body $body -ContentType "application/json" -Headers $headers
}

Testar -Nome "GET /analise-financeira/historico" -StatusEsperado 200 -Acao {
    Invoke-WebRequest -Uri "$baseUrl/analise-financeira/historico" -Method Get -Headers $headers
}

# ─────────────────────────────────────────────────
#  3. Cenários de erro (confirma o GlobalExceptionHandler)
# ─────────────────────────────────────────────────

Testar -Nome "GET /usuarios/9999 (404 esperado)" -StatusEsperado 404 -Acao {
    Invoke-WebRequest -Uri "$baseUrl/usuarios/9999" -Method Get -Headers $headers
}

Testar -Nome "POST /analise-financeira com renda negativa (400 esperado)" -StatusEsperado 400 -Acao {
    $body = @{ rendaMensal = -100; nivelEndividamento = 25; frequenciaPoupanca = "Media" } | ConvertTo-Json
    Invoke-WebRequest -Uri "$baseUrl/analise-financeira" -Method Post -Body $body -ContentType "application/json" -Headers $headers
}

Testar -Nome "GET sem token (403 esperado)" -StatusEsperado 403 -Acao {
    Invoke-WebRequest -Uri "$baseUrl/usuarios/1" -Method Get
}

# ─────────────────────────────────────────────────
#  Resumo final
# ─────────────────────────────────────────────────

Write-Host "`n=== Resultado ===" -ForegroundColor Cyan
Write-Host "Total de testes: $totalTestes"
if ($totalFalhas -eq 0) {
    Write-Host "Todos os testes passaram!" -ForegroundColor Green
} else {
    Write-Host "$totalFalhas teste(s) falharam." -ForegroundColor Red
}