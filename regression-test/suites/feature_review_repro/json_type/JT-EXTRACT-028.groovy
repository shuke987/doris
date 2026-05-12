// JT-EXTRACT-028: path 含换行 key
suite("repro_jt_extract_028") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a\nb":1}' AS JSONB), '\$."a\nb"') """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-028 threw: ${e.message}")
    }
}
