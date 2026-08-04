# ==============================================================================
# Serviço de predição — Smart Finance AI Service
# ==============================================================================
# Orquestra chamadas ao ModelPredictor e encapsula toda a lógica de negócio
# relacionada à inferência. Os controladores interagem exclusivamente com esta camada.
# ============================================================================== 

from __future__ import annotations

import logging
from typing import TYPE_CHECKING

from fastapi import HTTPException, status

from dto.schemas import (
    AnaliseFinanceiraRequest,
    AnaliseFinanceiraResponse,
    ClassificarRequest,
    ClassificarResponse,
)

if TYPE_CHECKING:
    from predictor import ModelPredictor

logger = logging.getLogger(__name__)

# Singleton em nível de módulo — inicializado pelo ciclo de vida do main.py.
_service: PredictionService | None = None


def init_service(predictor: ModelPredictor) -> None:
    """Chamado uma vez na inicialização da aplicação para criar a instância compartilhada do serviço."""
    global _service
    _service = PredictionService(predictor)
    logger.info("PredictionService initialized.")


def get_service() -> PredictionService:
    """Dependência FastAPI que retorna a instância compartilhada do PredictionService."""
    return _service


class PredictionService:
    """
    Camada de serviço da aplicação para inferência de ML.

    Encapsula o ModelPredictor para aplicar validação de entrada, tratamento de erros
    e mapeamento de respostas antes de devolver DTOs à camada de controllers.
    """

    def __init__(self, predictor: ModelPredictor) -> None:
        self._predictor = predictor

    # ------------------------------------------------------------------
    # Saúde
    # ------------------------------------------------------------------

    def health_status(self) -> dict:
        """Retorna o status atual de cada artefato de modelo carregado."""
        ready = self._predictor.is_ready()
        return {
            "status": "ok" if ready else "degraded",
            "models": self._predictor.models_status(),
            "ready": ready,
        }

    # ------------------------------------------------------------------
    # Classificação de transações
    # ------------------------------------------------------------------

    def classify_transaction(self, request: ClassificarRequest) -> ClassificarResponse:
        """
        Classifica a descrição de uma transação financeira usando o modelo de categoria.

        Levanta HTTP 503 se os artefatos de modelo necessários não estiverem carregados.
        """
        if not self._predictor.cat_model or not self._predictor.tfidf:
            raise HTTPException(
                status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
                detail="O modelo de classificação de transações não está disponível.",
            )

        categoria, confianca = self._predictor.predict_categoria(
            descricao=request.descricao,
            valor=request.valor if request.valor is not None else 0.0,
        )
        return ClassificarResponse(categoria=categoria, confianca=confianca)

    # ------------------------------------------------------------------
    # Análise do perfil financeiro
    # ------------------------------------------------------------------

    def analyze_financial_profile(
        self, request: AnaliseFinanceiraRequest
    ) -> AnaliseFinanceiraResponse:
        """
        Classifica o perfil financeiro do usuário usando o pipeline do modelo de perfil.

        Levanta HTTP 503 se o artefato de modelo necessário não estiver carregado.
        """
        
        if not self._predictor.perf_model:
            raise HTTPException(
                status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
                detail="O modelo de perfil financeiro não está disponível.",
            )

        perfil, probabilidade = self._predictor.predict_perfil(
            request.model_dump(by_alias=False)
        )
        return AnaliseFinanceiraResponse(perfil=perfil, probabilidade=probabilidade)
