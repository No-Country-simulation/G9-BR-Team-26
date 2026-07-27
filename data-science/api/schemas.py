# ==============================================================================
# Pydantic Schemas - Smart Finance AI Service
# ==============================================================================
# Este arquivo define os contratos de Entrada (Request) e Saída (Response) de dados
# utilizados pela API FastAPI para se comunicar com o backend Spring Boot.
#
# Responsabilidades a serem desenvolvidas:
# 1. ClassificarRequest: Schema para a descrição e valor de uma transação.
# 2. ClassificarResponse: Schema para retorno da categoria e nível de confiança.
# 3. AnaliseFinanceiraRequest: Schema para recebimento dos dados agregados e perfil do usuário.
# 4. ResumoGastosSchema: Schema para representação do resumo de gastos por categoria.
# 5. AnaliseFinanceiraResponse: Schema para o resultado da análise (perfil, probabilidade, resumo e recomendações).
# ==============================================================================

# TODAS AS IMPORTAÇÕES FUTURAS:
# from pydantic import BaseModel, Field
# from typing import List, Dict

# class ClassificarRequest(BaseModel):
#     descricao: str = Field(..., example="Ifood Burger King")
#     valor: float = Field(..., example=45.90)

# class ClassificarResponse(BaseModel):
#     categoria: str = Field(..., example="alimentacao")
#     confianca: float = Field(..., example=0.95)

# class ResumoGastosSchema(BaseModel):
#     alimentacao: float = 0.0
#     transporte: float = 0.0
#     lazer: float = 0.0
#     moradia: float = 0.0
#     saude: float = 0.0
#     educacao: float = 0.0
#     servicos: float = 0.0
#     outros: float = 0.0

# class AnaliseFinanceiraRequest(BaseModel):
#     renda_mensal: float = Field(..., example=4500.0)
#     nivel_endividamento: float = Field(..., example=25.0)
#     frequencia_poupanca: str = Field(..., example="Media") # "Baixa", "Media", "Alta"
#     gasto_alimentacao: float = 0.0
#     gasto_transporte: float = 0.0
#     gasto_lazer: float = 0.0
#     gasto_moradia: float = 0.0
#     gasto_saude: float = 0.0
#     gasto_educacao: float = 0.0
#     gasto_servicos: float = 0.0
#     gasto_outros: float = 0.0

# class AnaliseFinanceiraResponse(BaseModel):
#     perfil_financeiro: str = Field(..., example="Em observacao") # "Saudavel", "Em observacao", "Em risco"
#     probabilidade: float = Field(..., example=0.82)
#     resumo_gastos: ResumoGastosSchema
#     recomendacoes: List[str] = Field(..., example=["Monitorar gastos com lazer", "Aumentar reserva de emergência"])
