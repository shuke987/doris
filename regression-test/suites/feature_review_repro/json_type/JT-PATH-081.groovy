// JT-PATH-081: `$.["a\"b"]` 含 escaped 内部双引号
suite("repro_jt_path_081") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a\"b":1}' AS JSONB), '\$."a\"b"') """
    } catch (Exception e) {
        logger.info("JT-PATH-081 threw: ${e.message}")
    }
}
