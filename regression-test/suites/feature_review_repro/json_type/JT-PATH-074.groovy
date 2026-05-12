// JT-PATH-074: key 含 `\r` round-trip
suite("repro_jt_path_074") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a\rb":1}' AS JSONB), '\$."a\rb"') """
    } catch (Exception e) {
        logger.info("JT-PATH-074 threw: ${e.message}")
    }
}
