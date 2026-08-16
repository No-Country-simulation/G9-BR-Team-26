# ==============================================================================
# Configuração compartilhada dos testes — Smart Finance AI Service
# ==============================================================================
# Garante que os módulos da API (config, controller, dto, security, service,
# scripts) sejam importáveis a partir da raiz de `data-science/api`, exatamente
# como acontece em produção (uvicorn/gunicorn rodando dentro de `api/`), e
# fornece fixtures para testar os endpoints sem depender dos artefatos .joblib
# reais em disco.
# ==============================================================================

import os
import sys
from pathlib import Path

import pytest

API_DIR = Path(__file__).resolve().parents[1]
if str(API_DIR) not in sys.path:
    sys.path.insert(0, str(API_DIR))

# As variáveis de ambiente precisam existir antes de `config.settings` ser
# importado pela primeira vez (ele lê o token uma única vez, no import).
os.environ.setdefault("SMART_FINANCE_API_TOKEN", "test-token")
os.environ.setdefault("ALLOWED_ORIGINS", "http://localhost:3000")

from fastapi.testclient import TestClient  # noqa: E402

from main import app  # noqa: E402
from service.prediction_service import PredictionService, get_service  # noqa: E402


class FakePredictor:
    """
    Dublê de teste para ModelPredictor.

    Evita carregar os arquivos .joblib reais e permite simular tanto o
    cenário "modelos carregados" quanto "modelos indisponíveis" (503).
    """

    def __init__(self, loaded: bool = True):
        self.tfidf = object() if loaded else None
        self.cat_model = object() if loaded else None
        self.perf_model = object() if loaded else None

    def is_ready(self) -> bool:
        return self.cat_model is not None and self.perf_model is not None

    def models_status(self) -> dict:
        return {
            "tfidf": self.tfidf is not None,
            "categoria_model": self.cat_model is not None,
            "perfil_model": self.perf_model is not None,
        }

    def predict_categoria(self, descricao: str, valor: float = 0.0):
        return "Alimentacao", 0.95

    def predict_perfil(self, data: dict):
        return "Saudavel", 0.80


@pytest.fixture
def api_token() -> str:
    return os.environ["SMART_FINANCE_API_TOKEN"]


@pytest.fixture
def auth_headers(api_token: str) -> dict:
    return {"Authorization": f"Bearer {api_token}"}


def _client_with_predictor(loaded: bool) -> TestClient:
    fake_service = PredictionService(FakePredictor(loaded=loaded))
    app.dependency_overrides[get_service] = lambda: fake_service
    # Sem `with`, o lifespan (que carregaria os .joblib reais) não é executado —
    # os testes usam exclusivamente o FakePredictor via dependency override.
    return TestClient(app)


@pytest.fixture
def client_with_models_loaded():
    client = _client_with_predictor(loaded=True)
    yield client
    app.dependency_overrides.pop(get_service, None)


@pytest.fixture
def client_without_models():
    client = _client_with_predictor(loaded=False)
    yield client
    app.dependency_overrides.pop(get_service, None)
