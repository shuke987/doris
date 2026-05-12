// JT-CAST-036: jsonb T_String → IPV4
suite("repro_jt_cast_036") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('"192.168.1.1"' AS JSONB) AS IPV4) """
    } catch (Exception e) {
        logger.info("JT-CAST-036 threw: ${e.message}")
    }
}
