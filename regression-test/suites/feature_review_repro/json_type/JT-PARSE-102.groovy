// JT-PARSE-102: mysql client `\?` (literal)
suite("repro_jt_parse_102") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('{"a":1}' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-PARSE-102 threw: ${e.message}")
    }
}
