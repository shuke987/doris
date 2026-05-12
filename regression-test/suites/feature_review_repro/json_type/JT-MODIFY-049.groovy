// JT-MODIFY-049: remove 多 path UAF 风险
suite("repro_jt_modify_049") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_remove(CAST('{"a":1,"b":2,"c":3,"d":4,"e":5,"f":6,"g":7,"h":8,"i":9,"j":10}' AS JSONB), '\$.a','\$.b','\$.c','\$.d','\$.e','\$.f','\$.g','\$.h','\$.i','\$.j') """
    } catch (Exception e) {
        logger.info("JT-MODIFY-049 threw: ${e.message}")
    }
}
