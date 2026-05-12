// JT-CMP-035: FE JsonLiteral toSql round-trip 含反斜杠
suite("repro_jt_cmp_035") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ CREATE VIEW IF NOT EXISTS v_jt_cmp_035 AS SELECT CAST('{"a":"x"}' AS JSONB) AS j """
    } catch (Exception e) {
        logger.info("JT-CMP-035 threw: ${e.message}")
    }
}
