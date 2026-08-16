# ==============================================================================
# Testes — security.auth.verify_token
# ==============================================================================
# Testes unitários diretos sobre a dependência de autenticação, complementando
# os testes de integração (401 sem token / token errado) já cobertos em
# test_transaction.py e test_financial.py.
# ==============================================================================

import asyncio

import pytest
from fastapi import HTTPException
from fastapi.security import HTTPAuthorizationCredentials

from security.auth import verify_token
from config import settings


def test_verify_token_aceita_token_correto():
    credentials = HTTPAuthorizationCredentials(
        scheme="Bearer", credentials=settings.API_TOKEN
    )
    assert asyncio.run(verify_token(credentials)) is True


def test_verify_token_rejeita_token_incorreto():
    credentials = HTTPAuthorizationCredentials(
        scheme="Bearer", credentials="token-completamente-errado"
    )
    with pytest.raises(HTTPException) as exc_info:
        asyncio.run(verify_token(credentials))
    assert exc_info.value.status_code == 401
