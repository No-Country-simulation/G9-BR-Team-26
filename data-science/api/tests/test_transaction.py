# ==============================================================================
# Testes — POST /classificar
# ==============================================================================


def test_classificar_com_modelo_mockado_retorna_200(client_with_models_loaded, auth_headers):
    resp = client_with_models_loaded.post(
        "/classificar",
        json={"descricao": "Ifood Burger King", "valor": 45.90},
        headers=auth_headers,
    )

    assert resp.status_code == 200
    body = resp.json()
    assert body["categoria"] == "Alimentacao"
    assert 0.0 <= body["confianca"] <= 1.0


def test_classificar_sem_valor_usa_default(client_with_models_loaded, auth_headers):
    resp = client_with_models_loaded.post(
        "/classificar",
        json={"descricao": "Ifood Burger King"},
        headers=auth_headers,
    )
    assert resp.status_code == 200


def test_classificar_sem_modelo_carregado_retorna_503(client_without_models, auth_headers):
    resp = client_without_models.post(
        "/classificar",
        json={"descricao": "Ifood Burger King"},
        headers=auth_headers,
    )
    assert resp.status_code == 503


def test_classificar_sem_token_retorna_401(client_with_models_loaded):
    resp = client_with_models_loaded.post(
        "/classificar",
        json={"descricao": "Ifood Burger King"},
    )
    assert resp.status_code == 401


def test_classificar_token_invalido_retorna_401(client_with_models_loaded):
    resp = client_with_models_loaded.post(
        "/classificar",
        json={"descricao": "Ifood Burger King"},
        headers={"Authorization": "Bearer token-errado"},
    )
    assert resp.status_code == 401


def test_classificar_payload_invalido_retorna_422(client_with_models_loaded, auth_headers):
    # "descricao" é obrigatória — payload só com "valor" deve falhar a validação.
    resp = client_with_models_loaded.post(
        "/classificar",
        json={"valor": 10.0},
        headers=auth_headers,
    )
    assert resp.status_code == 422


def test_classificar_descricao_vazia_retorna_422(client_with_models_loaded, auth_headers):
    resp = client_with_models_loaded.post(
        "/classificar",
        json={"descricao": ""},
        headers=auth_headers,
    )
    assert resp.status_code == 422
