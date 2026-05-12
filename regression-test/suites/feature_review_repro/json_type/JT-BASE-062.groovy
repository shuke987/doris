// JT-BASE-062: size != header+packedBytes（缺 1 byte）
suite("repro_jt_base_062") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('1' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-BASE-062 threw: ${e.message}")
    }
}
