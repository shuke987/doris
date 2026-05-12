// JT-PATH-079: `$[last--5]` / `$[last+5]` 双负号 / 加号
suite("repro_jt_path_079") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[last--5]') """
    } catch (Exception e) {
        logger.info("JT-PATH-079 threw: ${e.message}")
    }
}
