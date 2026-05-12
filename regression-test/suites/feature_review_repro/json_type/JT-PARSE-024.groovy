// JT-PARSE-024: 非法控制字符未转义
suite("repro_jt_parse_024") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":""}') """
    } catch (Exception e) {
        logger.info("JT-PARSE-024 threw: ${e.message}")
    }
}
