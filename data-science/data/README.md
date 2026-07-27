# 📂 Módulo de Dados (CSVs de Treinamento)

Esta pasta contém os arquivos de dados em formato CSV utilizados pelos Jupyter Notebooks para o treinamento dos modelos `.joblib`.

### Arquivos CSV Esperados:
1. `transacoes_treino.csv`: Dataset contendo descrições textuais e categorias rotuladas para o **Modelo 1** (`modelo_categoria.joblib`).
   - Colunas principais: `descricao`, `valor`, `categoria`.
2. `perfis_usuarios_treino.csv`: Dataset sintético agregando histórico financeiro para o **Modelo 2** (`modelo_perfil.joblib`).
   - Colunas principais: `renda_mensal`, `nivel_endividamento`, `frequencia_poupanca`, gastos por categoria e `classe_perfil` (`Saudavel`, `Em observacao`, `Em risco`).

### 📌 Utilização:
Estes CSVs são lidos pelos notebooks em `data-science/notebooks/` para treinar e validar os modelos que serão exportados para `data-science/models/`.
