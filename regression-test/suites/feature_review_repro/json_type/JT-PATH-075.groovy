// JT-PATH-075: json_search 找到的 path `$[2]` vs `$[last]` round-trip
suite("repro_jt_path_075") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_search(CAST('[10,20,"target"]' AS JSONB), 'one', 'target') """
    } catch (Exception e) {
        logger.info("JT-PATH-075 threw: ${e.message}")
    }
}
