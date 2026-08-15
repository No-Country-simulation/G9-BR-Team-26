# ═══════════════════════════════════════════════════════════
#  Dockerfile — Finance AI Data Science API (FastAPI)
#  Imagem única (sem multi-stage, já que Python não compila
#  como Java — só precisamos instalar as dependências).
# ═══════════════════════════════════════════════════════════

FROM python:3.12-slim

WORKDIR /app

# Copia primeiro só o requirements.txt (aproveita cache do Docker:
# se o código mudar mas as dependências não, não reinstala tudo de novo).
COPY requirements.txt .

# Instala as dependências. --no-cache-dir evita guardar cache do pip
# dentro da imagem, deixando ela menor.
RUN pip install --no-cache-dir -r requirements.txt

# Copia o código da API e os modelos treinados.
COPY api ./api
COPY models ./models

# Muda para dentro da pasta da API, de onde o uvicorn/gunicorn é executado
# (mesmo comportamento documentado no api/README.md).
WORKDIR /app/api

# Porta que a API usa (documentação, não abre a porta sozinha).
EXPOSE 8000

# Comando de inicialização, usando gunicorn com workers uvicorn
# (padrão mais robusto para produção do que uvicorn sozinho).
CMD ["gunicorn", "main:app", "--workers", "2", "--worker-class", "uvicorn.workers.UvicornWorker", "--bind", "0.0.0.0:8000"]