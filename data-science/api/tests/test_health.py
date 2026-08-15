# ==============================================================================
# Testes — GET /health
# ==============================================================================


def test_health_com_modelos_carregados(client_with_models_loaded):
    resp = client_with_models_loaded.get("/health")

    assert resp.status_code == 200
    body = resp.json()
    assert body["status"] == "ok"
    assert body["ready"] is True
    assert body["models"] == {
        "tfidf": True,
        "categoria_model": True,
        "perfil_model": True,
    }


def test_health_sem_modelos_retorna_degraded(client_without_models):
    resp = client_without_models.get("/health")

    assert resp.status_code == 200
    body = resp.json()
    assert body["status"] == "degraded"
    assert body["ready"] is False
    assert body["models"] == {
        "tfidf": False,
        "categoria_model": False,
        "perfil_model": False,
    }


def test_health_nao_exige_autenticacao(client_with_models_loaded):
    # /health é público — não deve exigir token Bearer.
    resp = client_with_models_loaded.get("/health")
    assert resp.status_code != 401
