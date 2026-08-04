# ==============================================================================
# Objetos de transferência de dados — Smart Finance AI Service
# ==============================================================================
# Define os contratos de requisição e resposta para todos os endpoints da API.
# ============================================================================== 

from typing import Optional

from pydantic import BaseModel, Field


# ---------------------------------------------------------------------------
# /classificar
# ---------------------------------------------------------------------------

class ClassificarRequest(BaseModel):
    """Payload de requisição para classificação de transação."""

    descricao: str = Field(
        ...,
        min_length=1,
        max_length=500,
        description="Descrição textual da transação financeira.",
        examples=["Ifood Burger King"],
    )
    valor: Optional[float] = Field(
        None,
        ge=0,
        description=(
            "Valor monetário da transação em R$. "
            "Informar este campo melhora a precisão da classificação. "
            "Quando omitido, o modelo utiliza 0.0 como feature numérica."
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
    """Payload de resposta para classificação de transação."""

    categoria: str = Field(
        ...,
        description=(
            "Categoria prevista da transação. "
            "Valores possíveis: Alimentacao, Compras, Educacao, Impostos, "
            "Investimentos, Lazer, Moradia, Outros, Pets, Receitas, "
            "Saude, Servicos, Transporte."
        ),
        examples=["Alimentacao"],
    )
    confianca: float = Field(
        ...,
        ge=0.0,
        le=1.0,
        description="Pontuação de confiança da previsão, variando de 0.0 a 1.0.",
        examples=[0.99],
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "categoria": "Alimentacao",
                "confianca": 0.99,
            }
        }
    }


# ---------------------------------------------------------------------------
# /analise-financeira
# ---------------------------------------------------------------------------

class AnaliseFinanceiraRequest(BaseModel):
    """
    Payload de requisição para análise do perfil financeiro.

    Três campos são obrigatórios. O modelo subjacente utiliza internamente 60 features;
    os campos restantes são preenchidos com valores padrão neutros.
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
        description="Percentual da renda mensal comprometido com o pagamento de dívidas (0 a 100).",
        examples=[25.0],
    )
    frequencia_poupanca: str = Field(
        ...,
        alias="frequenciaPoupanca",
        description=(
            "Frequência com que o usuário poupa dinheiro. "
            "Valores aceitos: Sempre, Frequentemente, Media, As vezes, Raramente, Nunca."
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
    """Payload de resposta para análise do perfil financeiro."""

    perfil: str = Field(
        ...,
        description=(
            "Classe prevista do perfil financeiro. "
            "Valores possíveis: Saudavel, Em observacao, Em risco."
        ),
        examples=["Em observacao"],
    )
    probabilidade: float = Field(
        ...,
        ge=0.0,
        le=1.0,
        description="Probabilidade associada à classe prevista do perfil (0.0 a 1.0).",
    )

    model_config = {
        "json_schema_extra": {
            "example": {
                "perfil": "Em observacao",
                "probabilidade": 0.78,
            }
        }
    }
