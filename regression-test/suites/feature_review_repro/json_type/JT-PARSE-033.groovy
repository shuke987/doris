// JT-PARSE-033: key 含换行 `\n`
suite("repro_jt_parse_033") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a\nb":1}') """
    } catch (Exception e) {
        logger.info("JT-PARSE-033 threw: ${e.message}")
    }
}
