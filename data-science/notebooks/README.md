# 🧠 Módulo de Notebooks (Treinamento & Experimentos ML)

Esta pasta é destinada aos Jupyter Notebooks `.ipynb` para exploração de dados, engenharia de atributos e treinamento dos modelos.

### Arquivos Esperados:
- `01_treino_classificador_categoria.ipynb`: Treino do modelo de classificação de texto (TF-IDF + Classificador).
- `02_treino_perfil_financeiro.ipynb`: Treino do modelo de perfil de risco financeiro (Random Forest / Regressão Logística).

### ⚠️ Regra de Exportação:
Ao final da execução dos notebooks, os artefatos treinados **DEVEM obrigatoriamente ser salvos** no diretório `data-science/models/` com o comando:
```python
import joblib

joblib.dump(modelo_categoria, "../models/modelo_categoria.joblib")
joblib.dump(modelo_perfil, "../models/modelo_perfil.joblib")
```
