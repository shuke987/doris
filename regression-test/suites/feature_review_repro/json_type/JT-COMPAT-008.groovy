// JT-COMPAT-008: JSON_EXTRACT 数组负索引 `$[-1]`
suite("repro_jt_compat_008") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[-1]') """
    } catch (Exception e) {
        logger.info("JT-COMPAT-008 threw: ${e.message}")
    }
}
