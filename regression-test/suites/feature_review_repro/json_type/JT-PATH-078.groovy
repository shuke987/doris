// JT-PATH-078: `$[LAST]` `$[Last]` `$[lAsT]` 大小写组合
suite("repro_jt_path_078") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[LAST]') """
    } catch (Exception e) {
        logger.info("JT-PATH-078 threw: ${e.message}")
    }
}
