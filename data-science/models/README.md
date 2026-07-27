# 📦 Módulo de Modelos (.joblib)

Esta pasta armazena exclusivamente os artefatos serializados e os metadados gerados durante a etapa de treinamento nos notebooks.

### Arquivos Esperados:
- `modelo_categoria.joblib`: Pipeline treinado para classificar descrições de transações em categorias.
- `modelo_perfil.joblib`: Classificador treinado para prever a classe de perfil financeiro (`Saudavel`, `Em observacao`, `Em risco`).
- `classes.json`: Mapeamento de classes e rótulos auxiliares.

### 📌 Utilização:
Estes arquivos são gerados pela pasta `notebooks/` (via `joblib.dump`) e lidos pela API FastAPI em `data-science/api/predictor.py` para realizar a inferência em tempo real.