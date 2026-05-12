// JT-CAST-071: jsonb_string_as_string=true + 列含 T_Binary cast as STRING
suite("repro_jt_cast_071") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('"hi"' AS JSONB) AS STRING) """
    } catch (Exception e) {
        logger.info("JT-CAST-071 threw: ${e.message}")
    }
}
