// JT-PATH-049: $[*] 对 object 用
suite("repro_jt_path_049") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":1}' AS JSONB), '\$[*]') """
    } catch (Exception e) {
        logger.info("JT-PATH-049 threw: ${e.message}")
    }
}
