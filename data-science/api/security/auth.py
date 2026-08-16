# ==============================================================================
# Autenticação — Smart Finance AI Service
# ==============================================================================
# Fornece uma dependência FastAPI para verificação de token Bearer.
# O token esperado é carregado a partir da configuração e nunca é exposto na
# documentação da API nem nas respostas de erro.
# ============================================================================== 

import hmac

from fastapi import HTTPException, Security, status
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

from config.settings import API_TOKEN

_bearer_scheme = HTTPBearer(
    description="Token Bearer obrigatório. Entre em contato com o administrador da API para obter um token válido.",
    auto_error=True,
)


async def verify_token(
    credentials: HTTPAuthorizationCredentials = Security(_bearer_scheme),
) -> bool:
    """
    Valida o token Bearer informado no header Authorization.

    Levanta HTTP 401 se o token estiver ausente ou não corresponder ao valor configurado.
    O valor esperado do token é intencionalmente omitido de todas as respostas e da documentação.
    
    """
    if not API_TOKEN or not hmac.compare_digest(credentials.credentials, API_TOKEN):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Token de autenticação inválido ou ausente.",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return True
