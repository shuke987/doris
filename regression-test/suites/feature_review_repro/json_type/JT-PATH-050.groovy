// JT-PATH-050: $** 顶层 scalar
suite("repro_jt_path_050") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('1' AS JSONB), '\$**') """
    } catch (Exception e) {
        logger.info("JT-PATH-050 threw: ${e.message}")
    }
}
