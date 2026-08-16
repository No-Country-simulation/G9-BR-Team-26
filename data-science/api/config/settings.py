# ==============================================================================
# Configuração — Smart Finance AI Service
# ==============================================================================
# Centraliza todas as configurações da aplicação.
# Valores sensíveis são lidos a partir de variáveis de ambiente; os padrões são
# fornecidos apenas para desenvolvimento local e devem ser sobrescritos em produção.
# ============================================================================== 

import logging
import os
from pathlib import Path
from dotenv import load_dotenv

logger = logging.getLogger(__name__)

# Carrega o arquivo de ambiente da API.
ENV_FILE = Path(__file__).resolve().parents[1] / ".env"
load_dotenv(ENV_FILE)

API_TOKEN: str = os.getenv("SMART_FINANCE_API_TOKEN")

if not API_TOKEN:
    logger.warning(
        "SMART_FINANCE_API_TOKEN não está configurado. O serviço vai subir normalmente, "
        "mas TODAS as requisições aos endpoints protegidos falharão com 401 Unauthorized "
        "até que a variável de ambiente seja definida."
    )

# Diretório onde ficam os artefatos .joblib (tfidf, modelo_categoria, modelo_perfil)
BASE_DIR = Path(__file__).resolve().parents[2]  # aponta pra data-science/ (não api/)
MODELS_DIR = Path(os.getenv("MODELS_DIR", BASE_DIR / "models"))