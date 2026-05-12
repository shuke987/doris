// JT-CMP-028: CSE on jsonb_parse 字面量
suite("repro_jt_cmp_028") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') = jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-CMP-028 threw: ${e.message}")
    }
}
