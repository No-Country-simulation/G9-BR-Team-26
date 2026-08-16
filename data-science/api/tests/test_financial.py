# ==============================================================================
# Testes — POST /analise-financeira
# ==============================================================================

VALID_PAYLOAD = {
    "rendaMensal": 4500.0,
    "nivelEndividamento": 25,
    "frequenciaPoupanca": "Media",
}


def test_analise_financeira_com_modelo_mockado_retorna_200(
    client_with_models_loaded, auth_headers
):
    resp = client_with_models_loaded.post(
        "/analise-financeira", json=VALID_PAYLOAD, headers=auth_headers
    )

    assert resp.status_code == 200
    body = resp.json()
    assert body["perfil"] == "Saudavel"
    assert 0.0 <= body["probabilidade"] <= 1.0


def test_analise_financeira_sem_modelo_carregado_retorna_503(
    client_without_models, auth_headers
):
    resp = client_without_models.post(
        "/analise-financeira", json=VALID_PAYLOAD, headers=auth_headers
    )
    assert resp.status_code == 503


def test_analise_financeira_sem_token_retorna_401(client_with_models_loaded):
    resp = client_with_models_loaded.post("/analise-financeira", json=VALID_PAYLOAD)
    assert resp.status_code == 401


def test_analise_financeira_token_invalido_retorna_401(client_with_models_loaded):
    resp = client_with_models_loaded.post(
        "/analise-financeira",
        json=VALID_PAYLOAD,
        headers={"Authorization": "Bearer token-errado"},
    )
    assert resp.status_code == 401


def test_analise_financeira_payload_invalido_retorna_422(
    client_with_models_loaded, auth_headers
):
    # "rendaMensal" ausente e obrigatória.
    payload = {"nivelEndividamento": 25, "frequenciaPoupanca": "Media"}
    resp = client_with_models_loaded.post(
        "/analise-financeira", json=payload, headers=auth_headers
    )
    assert resp.status_code == 422


def test_analise_financeira_endividamento_fora_do_intervalo_retorna_422(
    client_with_models_loaded, auth_headers
):
    payload = {**VALID_PAYLOAD, "nivelEndividamento": 150}
    resp = client_with_models_loaded.post(
        "/analise-financeira", json=payload, headers=auth_headers
    )
    assert resp.status_code == 422
