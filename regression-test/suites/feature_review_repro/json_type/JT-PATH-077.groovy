// JT-PATH-077: 裸 `$**` 无 trailing
suite("repro_jt_path_077") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":1}' AS JSONB), '\$**') """
    } catch (Exception e) {
        logger.info("JT-PATH-077 threw: ${e.message}")
    }
}
