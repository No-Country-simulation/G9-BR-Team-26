# ==============================================================================
# Model Predictor - Smart Finance AI Service
# ==============================================================================
# Este arquivo contém a classe responsável por carregar os artefatos serializados (.joblib)
# da pasta models/ e executar a inferência de Machine Learning.
#
# Responsabilidades a serem desenvolvidas:
# 1. Carregar o modelo de classificação de categorias (modelo_categoria.joblib).
# 2. Carregar o modelo de perfil financeiro (modelo_perfil.joblib).
# 3. Implementar predict_categoria(descricao: str) -> Tuple[str, float]:
#    - Aplica pré-processamento/TF-IDF.
#    - Retorna a categoria predita e o score de confiança (predict_proba).
# 4. Implementar predict_perfil(dados: AnaliseFinanceiraRequest) -> Tuple[str, float]:
#    - Monta o vetor de atributos (renda, endividamento, encoding de poupança, gastos por categoria).
#    - Executa o modelo para classificar a classe ("Saudavel", "Em observacao", "Em risco").
#    - Retorna a classe e a probabilidade associada.
# ==============================================================================

# TODAS AS IMPORTAÇÕES FUTURAS:
# import joblib
# import os
# from typing import Tuple
# from api.schemas import AnaliseFinanceiraRequest

# class ModelPredictor:
#     def __init__(self, models_dir: str = "models"):
#         self.models_dir = models_dir
#         self.model_categoria = None
#         self.model_perfil = None

#     def load_models(self):
#         # Carrega modelo_categoria.joblib e modelo_perfil.joblib
#         pass

#     def predict_categoria(self, descricao: str) -> Tuple[str, float]:
#         # Retorna (categoria_predita, confianca)
#         pass

#     def predict_perfil(self, request: AnaliseFinanceiraRequest) -> Tuple[str, float]:
#         # Retorna (perfil_predito, probabilidade)
#         pass
