# Makefile for G9 Hackathon - Smart Finance

# Detect operating system
ifeq ($(OS),Windows_NT)
    MVN = .\backend\mvnw.cmd
    PYTHON = python
    PIP = pip
    VENV_ACTIVATE = .\data-science\venv\Scripts\activate
else
    MVN = mvn
    PYTHON = python3
    PIP = pip3
    VENV_ACTIVATE = . ./data-science/venv/bin/activate
endif

.PHONY: help db-up db-down db-clean db-logs db-status backend-build backend-run backend-test backend-clean ds-venv ds-install ds-run ds-clean up down

# Default target showing help
help:
	@echo "========================================================================"
	@echo "📊 SMART FINANCE - MAKEFILE COMMANDS"
	@echo "========================================================================"
	@echo "Database & Docker Commands:"
	@echo "  make db-up           - Start MySQL container in background"
	@echo "  make db-down         - Stop MySQL container"
	@echo "  make db-clean        - Stop MySQL container and remove volumes (delete data)"
	@echo "  make db-logs         - View MySQL container logs"
	@echo "  make db-status       - View status of docker containers"
	@echo ""
	@echo "Backend (Spring Boot) Commands:"
	@echo "  make backend-build   - Compile and package Spring Boot application"
	@echo "  make backend-run     - Run Spring Boot application locally"
	@echo "  make backend-test    - Run backend unit tests"
	@echo "  make backend-clean   - Clean backend target build directory"
	@echo ""
	@echo "Data Science (FastAPI) Commands:"
	@echo "  make ds-venv         - Create Python virtual environment"
	@echo "  make ds-install      - Install Python dependencies (requires requirements.txt)"
	@echo "  make ds-run          - Start FastAPI app with Uvicorn (dev mode)"
	@echo "  make ds-clean        - Clean Python build cache and virtual environment"
	@echo ""
	@echo "Combined Commands:"
	@echo "  make up              - Initialize database and run backend"
	@echo "  make down            - Stop and clean up all services"
	@echo "========================================================================"

# --- Database & Docker Compose ---
db-up:
	docker compose up -d

db-down:
	docker compose down

db-clean:
	docker compose down -v

db-logs:
	docker compose logs -f mysql

db-status:
	docker compose ps

# --- Backend (Spring Boot) ---
backend-build:
	mvn -f backend/pom.xml clean package -DskipTests

backend-run:
	mvn -f backend/pom.xml spring-boot:run

backend-test:
	mvn -f backend/pom.xml test

backend-clean:
	mvn -f backend/pom.xml clean

# --- Data Science (FastAPI) ---
ds-venv:
	$(PYTHON) -m venv data-science/venv

ds-install:
	@if [ -f data-science/requirements.txt ]; then \
		$(PYTHON) -m pip install --upgrade pip; \
		$(PIP) install -r data-science/requirements.txt; \
	else \
		echo "No requirements.txt found in data-science/"; \
	fi

ds-run:
	@echo "Starting FastAPI server..."
	@cd data-science && uvicorn app.main:app --reload --port 8000

ds-clean:
	@rm -rf data-science/venv
	@find . -type d -name "__pycache__" -exec rm -r {} + 2>/dev/null || true

# --- Global / Combined ---
up: db-up backend-run

down: db-down
