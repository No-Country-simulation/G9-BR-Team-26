# ==============================================================================
# FastAPI Main Application - Smart Finance AI Service
# ==============================================================================
# Este arquivo é o ponto de entrada da API FastAPI.
# 
# Responsabilidades a serem desenvolvidas:
# 1. Inicializar a aplicação FastAPI.
# 2. Configurar eventos de startup para carregar os modelos .joblib em memória (via Predictor).
# 3. Definir o endpoint GET /health para health check.
# 4. Definir o endpoint POST /classificar que recebe a transação e retorna a categoria predita.
# 5. Definir o endpoint POST /analise-financeira que recebe os dados financeiros agregados do usuário,
#    chama a predição de perfil e aplica o motor de regras de recomendação.
# ==============================================================================

# TODAS AS IMPORTAÇÕES FUTURAS:
# from fastapi import FastAPI, HTTPException, status
# from api.schemas import ClassificarRequest, ClassificarResponse, AnaliseFinanceiraRequest, AnaliseFinanceiraResponse
# from api.predictor import ModelPredictor
# from api.rules import RecommendationEngine

# Instância do FastAPI (a ser ativada):
# app = FastAPI(title="Smart Finance AI API", version="1.0.0")

# predictor = ModelPredictor()
# rules_engine = RecommendationEngine()

# @app.on_event("startup")
# def load_models():
#     # Carregar modelos .joblib da pasta models/
#     pass

# @app.get("/health")
# def health_check():
#     return {"status": "ok"}

# @app.post("/classificar", response_model=ClassificarResponse)
# def classificar_transacao(request: ClassificarRequest):
#     # 1. Extrair a descrição e valor da requisição enviado pelo Spring Boot
#     # 2. Chamar predictor.predict_categoria(request.descricao)
#     # 3. Retornar categoria e confiança
#     pass

# @app.post("/analise-financeira", response_model=AnaliseFinanceiraResponse)
# def analisar_perfil_financeiro(request: AnaliseFinanceiraRequest):
#     # 1. Receber renda, endividamento, poupança e gastos agregados enviados pelo Spring Boot
#     # 2. Chamar predictor.predict_perfil(request)
#     # 3. Gerar recomendações personalizadas com rules_engine.generate_recommendations(perfil, request)
#     # 4. Retornar perfil, probabilidade, resumo_gastos e recomendações
#     pass
