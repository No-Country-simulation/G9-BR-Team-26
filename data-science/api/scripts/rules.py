# from typing import Any, Dict, List


# class RecommendationEngine:
#     def __init__(self):
#         pass

#     def generate_recommendations(self, perfil: str, data: Dict[str, Any]) -> List[str]:
#         recs: List[str] = []
#         renda = float(data.get("renda", 0.0) or 0.0)
#         despesas = float(data.get("despesas", 0.0) or 0.0)
#         dividas = float(data.get("dividas", 0.0) or 0.0)
#         poupanca = float(data.get("poupanca", 0.0) or 0.0)

#         # Simple rule engine examples
#         if perfil.lower() in ("conservador", "baixo"):
#             recs.append("Priorize reserva de emergência de 3-6 meses de despesas.")
#         if dividas > (renda * 0.3):
#             recs.append("Considere renegociar dívidas com juros altos.")
#         if despesas > renda:
#             recs.append("Reduza gastos não essenciais para equilibrar o orçamento.")
#         if poupanca < (renda * 0.1):
#             recs.append("Aumente a taxa de poupança para pelo menos 10% da renda.")
#         if not recs:
#             recs.append("Nenhuma recomendação específica; mantenha bons hábitos financeiros.")
#         return recs
# # ==============================================================================
# # Motor de recomendações — Smart Finance AI Service
# # ==============================================================================
# # Este arquivo contém a lógica de negócios para geração de recomendações
# # financeiras personalizadas com base na classe de perfil predita pela IA e
# # nas métricas de gastos do usuário.
# #
# # Responsabilidades a serem desenvolvidas:
# # 1. Avaliar a classe do perfil financeiro ("Saudavel", "Em observacao", "Em risco").
# # 2. Calcular o percentual comprometido com dívidas e com categorias não essenciais (por exemplo, lazer).
# # 3. Gerar uma lista de recomendações personalizadas:
# #    - Exemplo 'Em risco': "Priorizar quitação de dívidas com juros altos", "Reduzir gastos com lazer".
# #    - Exemplo 'Em observacao': "Controlar pequenas despesas recorrentes", "Formar reserva de emergência".
# #    - Exemplo 'Saudavel': "Manter o ritmo de poupança", "Avaliar opções de investimentos de médio prazo".
# # ============================================================================== 

# # TODAS AS IMPORTAÇÕES FUTURAS:
# # from typing import List
# # from api.schemas import AnaliseFinanceiraRequest

# # class RecommendationEngine:
# #     def generate_recommendations(self, perfil: str, dados: AnaliseFinanceiraRequest) -> List[str]:
# #         # Aplica um motor de regras determinísticas combinado com o perfil de IA
# #         return recommendations
