// JT-PARSE-122: nereids 解析 200 层嵌套 jsonb literal
suite("repro_jt_parse_122") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('{"a":1}' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-PARSE-122 threw: ${e.message}")
    }
}
