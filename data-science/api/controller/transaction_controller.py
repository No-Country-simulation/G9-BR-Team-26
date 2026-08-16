# ==============================================================================
# Controlador de transações — Smart Finance AI Service
# ============================================================================== 

from typing import Any

from fastapi import APIRouter, Depends

from dto.schemas import ClassificarRequest, ClassificarResponse
from security.auth import verify_token
from service.prediction_service import PredictionService, get_service

router = APIRouter(tags=["transactions"])


@router.post(
    "/classificar",
    response_model=ClassificarResponse,
    summary="Classificar uma transação",
    description=(
        "Recebe a descrição e o valor monetário opcional de uma transação financeira "
        "e retorna a categoria prevista com sua pontuação de confiança associada.\n\n"
        "O modelo de classificação utiliza 488 features de entrada: 487 features de texto TF-IDF "
        "derivadas da descrição da transação, mais uma feature numérica para o valor da transação.\n\n"
        "Quando o campo de valor é omitido, 0.0 é usado como feature numérica.\n\n"
        "Categorias suportadas: Alimentacao, Compras, Educacao, Impostos, Investimentos, "
        "Lazer, Moradia, Outros, Pets, Receitas, Saude, Servicos, Transporte."
    ),
    response_description="Categoria prevista e pontuação de confiança.",
    responses={
        200: {
            "description": "Transação classificada com sucesso.",
            "content": {
                "application/json": {
                    "examples": {
                        "food": {
                            "summary": "Transação de alimentação",
                            "value": {"categoria": "Alimentacao", "confianca": 0.99},
                        },
                        "transport": {
                            "summary": "Transação de transporte",
                            "value": {"categoria": "Transporte", "confianca": 0.87},
                        },
                        "utilities": {
                            "summary": "Transação de utilidades",
                            "value": {"categoria": "Servicos", "confianca": 0.74},
                        },
                    }
                }
            },
        },
        401: {
            "description": "Token de autenticação inválido ou ausente.",
            "content": {
                "application/json": {
                    "example": {"detail": "Token de autenticação inválido ou ausente."}
                }
            },
        },
        422: {
            "description": "Erro de validação do payload da requisição.",
        },
        503: {
            "description": "Artefato do modelo de classificação não carregado.",
            "content": {
                "application/json": {
                    "example": {"detail": "O modelo de classificação de transações não está disponível."}
                }
            },
        },
    },
)

def classificar_transacao(
    request: ClassificarRequest,
    authorized: Any = Depends(verify_token),
    service: PredictionService = Depends(get_service),
) -> ClassificarResponse:
    return service.classify_transaction(request)
