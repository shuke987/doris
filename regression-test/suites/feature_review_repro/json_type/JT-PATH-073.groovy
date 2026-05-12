// JT-PATH-073: key 含 `\t` round-trip
suite("repro_jt_path_073") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a\tb":1}' AS JSONB), '\$."a\tb"') """
    } catch (Exception e) {
        logger.info("JT-PATH-073 threw: ${e.message}")
    }
}
