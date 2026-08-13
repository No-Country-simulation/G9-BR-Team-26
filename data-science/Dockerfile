# ═══════════════════════════════════════════════════════════
#  Dockerfile — Data Science AI Service (FastAPI)
# ═══════════════════════════════════════════════════════════

# Imagem base oficial do Python 3.11 (versão slim para menor tamanho e maior segurança)
FROM python:3.11-slim

# Variáveis de ambiente para otimizar o Python no container
ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    PYTHONPATH=/app/api

# Diretório raiz de trabalho no container
WORKDIR /app

# Copia o arquivo de dependências para aproveitar o cache de camadas do Docker
COPY requirements.txt .

# Atualiza o pip e instala as dependências do projeto
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# Copia todo o código-fonte (incluindo api/ e os modelos em models/)
COPY . .

# Define o diretório de trabalho na pasta da API
WORKDIR /app/api

# Documenta a porta na qual a API de Data Science irá rodar
EXPOSE 8000

# Inicializa o servidor Uvicorn escutando em todas as interfaces de rede na porta 7070
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
