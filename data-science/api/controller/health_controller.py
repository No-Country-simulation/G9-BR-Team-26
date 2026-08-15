# ==============================================================================
# Controlador de saúde — Smart Finance AI Service
# ============================================================================== 

from fastapi import APIRouter, Depends

from service.prediction_service import PredictionService, get_service

router = APIRouter(tags=["health"])


@router.get(
    "/health",
    summary="Verificação de saúde",
    description=(
        "Retorna o status operacional da API e o estado de carregamento de cada artefato de modelo. "
        "Use este endpoint para verificar se todos os arquivos .joblib necessários foram carregados corretamente na inicialização."
    ),
    response_description="Status da API e disponibilidade por modelo.",
    responses={
        200: {
            "description": "Status recuperado com sucesso.",
            "content": {
                "application/json": {
                    "examples": {
                        "all_loaded": {
                            "summary": "Todos os modelos carregados",
                            "value": {
                                "status": "ok",
                                "models": {
                                    "tfidf": True,
                                    "categoria_model": True,
                                    "perfil_model": True,
                                },
                                "ready": True,
                            },
                        },
                        "degraded": {
                            "summary": "Um ou mais modelos indisponíveis",
                            "value": {
                                "status": "degraded",
                                "models": {
                                    "tfidf": True,
                                    "categoria_model": True,
                                    "perfil_model": False,
                                },
                                "ready": False,
                            },
                        },
                    }
                }
            },
        }
    },
)
def health_check(service: PredictionService = Depends(get_service)) -> dict:
    return service.health_status()
