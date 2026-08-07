# ==============================================================================
# Ponto de entrada da aplicação — Smart Finance AI Service
# ==============================================================================
# Responsável por:
#   - Definir o ciclo de vida da aplicação (carregamento dos modelos na inicialização).
#   - Compor a aplicação FastAPI com metadados e middleware.
#   - Registrar todos os roteadores da camada de controllers.
#
# Execute com:
# ==============================================================================

import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from config.settings import MODELS_DIR
from controller import financial_controller, health_controller, transaction_controller
from scripts.predictor import ModelPredictor
from service.prediction_service import init_service

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# ==============================================================================
# Ciclo de vida
# ============================================================================== 

@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Manipulador do ciclo de vida da aplicação.

    Na inicialização: carrega todos os artefatos de modelo do diretório configurado
    e inicializa o singleton PredictionService.
    Na finalização: registra o encerramento da aplicação.
    """
    logger.info("[startup] Carregando artefatos de modelo de: %s", MODELS_DIR)
    predictor = ModelPredictor(models_dir=MODELS_DIR)
    predictor.load_models()
    init_service(predictor)
    logger.info("[startup] Aplicação pronta.")
    yield
    logger.info("[shutdown] Aplicação encerrada.")

# ==============================================================================
# Metadados do OpenAPI
# ============================================================================== 

_DESCRIPTION = """
A API Smart Finance AI fornece dois endpoints de inferência por machine learning integrados à plataforma Smart Finance.

## Classificação de transações

O endpoint /classificar classifica a descrição de uma transação financeira em uma de 13 categorias predefinidas \
utilizando um modelo Multinomial Naive Bayes treinado com vetorização TF-IDF \
(488 features: 487 TF-IDF + 1 coluna numérica).

Categorias suportadas: Alimentacao, Compras, Educacao, Impostos, Investimentos, Lazer, \
Moradia, Outros, Pets, Receitas, Saude, Servicos, Transporte.

## Análise do perfil financeiro

O endpoint /analise-financeira classifica o perfil financeiro do usuário em uma de três classes \
com base na renda mensal, no nível de endividamento e na frequência de poupança, \
utilizando um pipeline Random Forest (60 features de entrada).

Perfis retornados: Saudavel, Em observacao, Em risco.

## Autenticação

Todos os endpoints protegidos exigem um token Bearer válido informado no header Authorization. \
Entre em contato com o administrador da API para obter um token.

## Códigos de resposta HTTP

- 200: Requisição processada com sucesso.
- 401: Token de autenticação inválido ou ausente.
- 422: Falha de validação do payload da requisição.
- 503: Artefato de modelo necessário não carregado.
"""

_TAGS_METADATA = [
    {
        "name": "health",
        "description": "Retorna o status operacional da API e o estado de carregamento de cada artefato de modelo.",
    },
    {
        "name": "transactions",
        "description": (
            "Classifica descrições de transações financeiras em categorias predefinidas. "
            "Modelo: Multinomial Naive Bayes com vetorização TF-IDF."
        ),
    },
    {
        "name": "financial-profile",
        "description": (
            "Classifica o perfil financeiro do usuário com base em renda, nível de endividamento e comportamento de poupança. "
            "Modelo: pipeline Random Forest."
        ),
    },
]


# ==============================================================================
# Aplicação FastAPI

app = FastAPI(
    title="Smart Finance AI API",
    description=_DESCRIPTION,
    version="1.0.0",
    lifespan=lifespan,
    openapi_tags=_TAGS_METADATA,
    contact={
        "name": "G9-BR Team 26",
        "url": "https://github.com/No-Country-simulation/G9-BR-Team-26",
    },
)


# ==============================================================================
# Middleware
# ==============================================================================

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# ==============================================================================
# Roteadores
# ==============================================================================

app.include_router(health_controller.router)
app.include_router(transaction_controller.router)
app.include_router(financial_controller.router)