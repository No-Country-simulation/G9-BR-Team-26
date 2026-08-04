# ==============================================================================
# Model Predictor - Smart Finance AI Service
# ==============================================================================
# Responsável por carregar os artefatos serializados (.joblib) da pasta models/
# e executar a inferência dos dois modelos de Machine Learning:
#
#   1. modelo_categoria.joblib  — MultinomialNB
#      Input : hstack([tfidf.transform([descricao]), [[valor]]]) → (1, 488)
#      Output: categoria da transação + confiança (predict_proba)
#
#   2. modelo_perfil.joblib     — Pipeline (ColumnTransformer + RandomForest)
#      Input : pd.DataFrame com 60 colunas nomeadas
#      Output: perfil financeiro + probabilidade (predict_proba)
#
# Apenas rendaMensal, nivelEndividamento e frequenciaPoupanca são fornecidos
# pelo cliente; as demais 57 features do modelo de perfil recebem valor 0.0.
# ==============================================================================

from __future__ import annotations

import logging
from pathlib import Path
from typing import Any, Dict, Tuple

import joblib
import numpy as np
import pandas as pd
from scipy.sparse import hstack

logger = logging.getLogger(__name__)

# ---------------------------------------------------------------------------
# Nomes exatos das 60 colunas que o pipeline de perfil espera.
# Atenção: alguns nomes contêm acentos/caracteres especiais — devem ser
# idênticos aos usados no treinamento.
# ---------------------------------------------------------------------------
_PERFIL_FEATURE_NAMES: list[str] = [
    "idade",
    "sexo",
    "estado_civil",
    "dependentes",
    "escolaridade",
    "renda_mensal_x",
    "outras_rendas",
    "nivel_endividamento",
    "score_credito",
    "saldo_medio_conta",
    "limite_cartao",
    "frequencia_poupanca",
    "percentual_renda_investida",
    "tempo_emprego_anos",
    "possui_financiamento",
    "possui_imovel",
    "possui_veiculo",
    "gasto_total",
    "receita_total",
    "numero_transacoes",
    "ticket_medio",
    "maior_despesa",
    "menor_despesa",
    "saldo",
    "renda_mensal_y",
    "razao_gasto_renda",
    "gasto_alimentação",
    "gasto_compras",
    "gasto_educação",
    "gasto_impostos",
    "gasto_investimentos",
    "gasto_lazer",
    "gasto_moradia",
    "gasto_outros",
    "gasto_pets",
    "gasto_saúde",
    "gasto_serviços",
    "gasto_transporte",
    "perc_total",
    "perc_alimentação",
    "perc_compras",
    "perc_educação",
    "perc_impostos",
    "perc_investimentos",
    "perc_lazer",
    "perc_moradia",
    "perc_outros",
    "perc_pets",
    "perc_saúde",
    "perc_serviços",
    "perc_transporte",
    "qtd_recorrentes",
    "qtd_parceladas",
    "perc_boleto",
    "perc_cartão de crédito",
    "perc_cartão de débito",
    "perc_dinheiro",
    "perc_débito automático",
    "perc_pix",
    "perc_transferência bancária",
]

# Defaults para colunas categóricas esperadas pelo OneHotEncoder
_PERFIL_CAT_DEFAULTS: Dict[str, str] = {
    "sexo": "Masculino",
    "estado_civil": "Solteiro",
    "escolaridade": "Médio",
}

# Mapeamento de frequenciaPoupanca (valores aceitos pelo modelo)
_FREQUENCIA_MAP: Dict[str, str] = {
    # valores normalizados → label do modelo
    "sempre": "Sempre",
    "frequentemente": "Frequentemente",
    "as vezes": "Às vezes",
    "às vezes": "Às vezes",
    "media": "Às vezes",      # "Media" mapeada para "Às vezes"
    "média": "Às vezes",
    "raramente": "Raramente",
    "nunca": "Nunca",
}


class ModelPredictor:
    """Carrega e executa os modelos de ML do Smart Finance AI."""

    def __init__(self, models_dir: Path | str | None = None):
        self.models_dir: Path | None = Path(models_dir) if models_dir else None
        self.tfidf = None          # TfidfVectorizer
        self.cat_model = None      # MultinomialNB  (categoria)
        self.perf_model = None     # Pipeline       (perfil financeiro)

    # ------------------------------------------------------------------
    # Carregamento
    # ------------------------------------------------------------------

    def load_models(self) -> None:
        """Carrega os três artefatos .joblib em memória."""
        base = (
            Path(self.models_dir)
            if self.models_dir
            else Path(__file__).resolve().parents[1] / "models"
        )

        self._safe_load("tfidf",      base / "tfidf.joblib",           "tfidf")
        self._safe_load("cat_model",  base / "modelo_categoria.joblib", "cat_model")
        self._safe_load("perf_model", base / "modelo_perfil.joblib",    "perf_model")

        logger.info(
            "Modelos carregados — tfidf=%s | categoria=%s | perfil=%s",
            self.tfidf is not None,
            self.cat_model is not None,
            self.perf_model is not None,
        )

    def _safe_load(self, label: str, path: Path, attr: str) -> None:
        """Carrega um arquivo joblib com tratamento de erros."""
        if not path.exists():
            logger.warning("Arquivo não encontrado: %s", path)
            return
        try:
            setattr(self, attr, joblib.load(path))
            logger.info("✔ %s carregado de %s", label, path.name)
        except Exception as exc:
            logger.error("Falha ao carregar %s: %s", label, exc)

    # ------------------------------------------------------------------
    # Status
    # ------------------------------------------------------------------

    def is_ready(self) -> bool:
        """Retorna True se ambos os modelos principais estão carregados."""
        return self.cat_model is not None and self.perf_model is not None

    def models_status(self) -> Dict[str, bool]:
        return {
            "tfidf": self.tfidf is not None,
            "categoria_model": self.cat_model is not None,
            "perfil_model": self.perf_model is not None,
        }

    # ------------------------------------------------------------------
    # Predição — Categoria de transação
    # ------------------------------------------------------------------

    def predict_categoria(
        self, descricao: str, valor: float = 0.0
    ) -> Tuple[str, float]:
        """
        Classifica a descrição de uma transação.

        O modelo MultinomialNB foi treinado com 488 features:
        487 do TF-IDF + 1 coluna numérica (valor da transação).

        Parameters
        ----------
        descricao : str
            Descrição da transação (ex.: "Ifood Burger King").
        valor : float
            Valor monetário da transação. Default: 0.0.

        Returns
        -------
        (categoria, confiança) : Tuple[str, float]
        """
        if self.cat_model is None or self.tfidf is None:
            logger.warning("Modelos de categoria não disponíveis.")
            return "desconhecida", 0.0

        X_text = self.tfidf.transform([descricao])           # (1, 487)
        X_valor = np.array([[float(valor)]])                  # (1, 1)
        X_final = hstack([X_text, X_valor])                  # (1, 488)

        probs = self.cat_model.predict_proba(X_final)[0]
        idx = int(np.argmax(probs))
        label = str(self.cat_model.classes_[idx])
        return label, float(probs[idx])

    # ------------------------------------------------------------------
    # Predição — Perfil financeiro
    # ------------------------------------------------------------------

    def predict_perfil(self, data: Dict[str, Any]) -> Tuple[str, float]:
        """
        Classifica o perfil financeiro do usuário.

        O pipeline foi treinado com 60 features. Apenas 3 são recebidas
        pelo endpoint da API; as demais recebem valores default (0 / string
        categórica padrão).

        Parameters
        ----------
        data : dict
            Deve conter as chaves:
            - rendaMensal / renda_mensal (float)
            - nivelEndividamento / nivel_endividamento (float, percentual 0-100)
            - frequenciaPoupanca / frequencia_poupanca (str)

        Returns
        -------
        (perfil, probabilidade) : Tuple[str, float]
        """
        if self.perf_model is None:
            logger.warning("Modelo de perfil não disponível.")
            return "desconhecido", 0.0

        renda = float(
            data.get("rendaMensal") or data.get("renda_mensal") or 0.0
        )
        endividamento = float(
            data.get("nivelEndividamento") or data.get("nivel_endividamento") or 0.0
        )
        freq_raw = str(
            data.get("frequenciaPoupanca") or data.get("frequencia_poupanca") or ""
        )
        frequencia = _FREQUENCIA_MAP.get(freq_raw.lower().strip(), "Raramente")

        # Monta linha com todos os defaults
        row: Dict[str, Any] = {col: 0.0 for col in _PERFIL_FEATURE_NAMES}

        # Sobrescreve com os 3 campos recebidos (mapeia para os dois aliases)
        row["renda_mensal_x"] = renda
        row["renda_mensal_y"] = renda
        row["nivel_endividamento"] = endividamento
        row["frequencia_poupanca"] = frequencia

        # Defaults de colunas categóricas (OneHotEncoder exige strings válidas)
        for col, default in _PERFIL_CAT_DEFAULTS.items():
            row[col] = default

        df = pd.DataFrame([row])[_PERFIL_FEATURE_NAMES]

        probs = self.perf_model.predict_proba(df)[0]
        idx = int(np.argmax(probs))
        label = str(self.perf_model.classes_[idx])
        return label, float(probs[idx])