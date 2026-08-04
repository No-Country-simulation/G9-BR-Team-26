# ==============================================================================
# Pydantic Schemas — Smart Finance AI Service
# ==============================================================================
# Define os contratos de entrada (Request) e saída (Response) da API.
# ==============================================================================

from typing import Optional

from pydantic import BaseModel, Field


# ---------------------------------------------------------------------------
# /classificar
# ---------------------------------------------------------------------------

class ClassificarRequest(BaseModel):
    """Payload para classificação de uma transação financeira."""

    descricao: str = Field(
        ...,
        min_length=1,
        max_length=500,
        description="Descrição textual da transação (ex.: nome do estabelecimento ou serviço).",
        examples=["Ifood Burger King"],
    )
    valor: Optional[float] = Field(
        None,
        ge=0,
        description=(
            "Valor monetário da transação em R$. "
            "Recomendado para maior acurácia — quando omitido, usa 0.0 como fallback."
        ),
        examples=[45.90],
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "descricao": "Ifood Burger King",
                "valor": 45.90,
            }
        }
    }


class ClassificarResponse(BaseModel):
    """Resultado da classificação de uma transação."""

    categoria: str = Field(
        ...,
        description=(
            "Categoria predita pelo modelo. Valores possíveis: "
            "Alimentação, Compras, Educação, Impostos, Investimentos, "
            "Lazer, Moradia, Outros, Pets, Receitas, Saúde, Serviços, Transporte."
        ),
        examples=["Alimentação"],
    )
    confianca: float = Field(
        ...,
        ge=0.0,
        le=1.0,
        description="Score de confiança da predição (probabilidade entre 0 e 1).",
        examples=[0.99],
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "categoria": "Alimentação",
                "confianca": 0.99,
            }
        }
    }


# ---------------------------------------------------------------------------
# /analise-financeira
# ---------------------------------------------------------------------------

class AnaliseFinanceiraRequest(BaseModel):
    """
    Payload para análise do perfil financeiro.

    Apenas três campos são obrigatórios. O modelo interno utiliza
    60 features; os demais são preenchidos automaticamente com defaults.
    """

    renda_mensal: float = Field(
        ...,
        alias="rendaMensal",
        gt=0,
        description="Renda mensal bruta do usuário em R$.",
        examples=[4500.0],
    )
    nivel_endividamento: float = Field(
        ...,
        alias="nivelEndividamento",
        ge=0,
        le=100,
        description="Percentual da renda comprometida com dívidas (0–100).",
        examples=[25.0],
    )
    frequencia_poupanca: str = Field(
        ...,
        alias="frequenciaPoupanca",
        description=(
            "Frequência com que o usuário realiza poupança. "
            "Valores aceitos: Sempre, Frequentemente, Media, Às vezes, Raramente, Nunca."
        ),
        examples=["Media"],
    )

    model_config = {
        "populate_by_name": True,
        "json_schema_extra": {
            "example": {
                "rendaMensal": 4500.0,
                "nivelEndividamento": 25,
                "frequenciaPoupanca": "Media",
            }
        },
    }


class AnaliseFinanceiraResponse(BaseModel):
    """Resultado da análise do perfil financeiro."""

    perfil: str = Field(
        ...,
        description=(
            "Perfil financeiro classificado pelo modelo. "
            "Valores possíveis: Saudável, Em observação, Em risco."
        ),
        examples=["Em observação"],
    )
    probabilidade: float = Field(
        ...,
        ge=0.0,
        le=1.0,
        description="Probabilidade associada ao perfil predito (entre 0 e 1).",
        examples=[0.78],
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "perfil": "Em observação",
                "probabilidade": 0.78,
            }
        }
    }
