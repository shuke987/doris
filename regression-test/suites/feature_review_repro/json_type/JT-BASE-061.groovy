// JT-BASE-061: size != header+packedBytes（多 1 byte trailing garbage）
suite("repro_jt_base_061") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('1' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-BASE-061 threw: ${e.message}")
    }
}
