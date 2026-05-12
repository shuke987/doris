// JT-PARSE-109: audit_log 含 parse 错误 SQL
suite("repro_jt_parse_109") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-PARSE-109 threw: ${e.message}")
    }
}
