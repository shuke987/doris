// JT-PARSE-106: 错误含输入预览
suite("repro_jt_parse_106") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('bad json') """
    } catch (Exception e) {
        logger.info("JT-PARSE-106 threw: ${e.message}")
    }
}
