# ==============================================================================
# Controlador de perfil financeiro — Smart Finance AI Service
# ============================================================================== 

from typing import Any

from fastapi import APIRouter, Depends

from dto.schemas import AnaliseFinanceiraRequest, AnaliseFinanceiraResponse
from security.auth import verify_token
from service.prediction_service import PredictionService, get_service

router = APIRouter(tags=["financial-profile"])


@router.post(
    "/analise-financeira",
    response_model=AnaliseFinanceiraResponse,
    summary="Analisar perfil financeiro",
    description=(
        "Recebe a renda mensal, o nível de endividamento e a frequência de poupança do usuário, "
        "e retorna a classe do perfil financeiro previsto com sua probabilidade associada.\n\n"
        "O pipeline Random Forest subjacente utiliza 60 features de entrada. "
        "Apenas três são exigidas do cliente; as features restantes "
        "são preenchidas internamente com valores padrão neutros.\n\n"
        "O campo de frequência de poupança aceita os seguintes valores: "
        "Sempre, Frequentemente, Media, As vezes, Raramente, Nunca.\n\n"
        "Classes de perfil retornadas: Saudavel, Em observacao, Em risco."
    ),
    response_description="Classe do perfil previsto e probabilidade associada.",
    responses={
        200: {
            "description": "Análise do perfil concluída com sucesso.",
            "content": {
                "application/json": {
                    "examples": {
                        "saudavel": {
                            "summary": "Perfil financeiro saudável",
                            "value": {"perfil": "Saudavel", "probabilidade": 0.72},
                        },
                        "observacao": {
                            "summary": "Perfil em observação",
                            "value": {"perfil": "Em observacao", "probabilidade": 0.61},
                        },
                        "risco": {
                            "summary": "Perfil em risco",
                            "value": {"perfil": "Em risco", "probabilidade": 0.85},
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
            "description": "Artefato do modelo de perfil financeiro não carregado.",
            "content": {
                "application/json": {
                    "example": {"detail": "O modelo de perfil financeiro não está disponível."}
                }
            },
        },
    },
)
def analisar_perfil_financeiro(
    request: AnaliseFinanceiraRequest,
    authorized: Any = Depends(verify_token),
    service: PredictionService = Depends(get_service),
) -> AnaliseFinanceiraResponse:
    return service.analyze_financial_profile(request)
