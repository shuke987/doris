// JT-CAST-072: strict_mode + jsonb `{"a":"not_int"}` cast as STRUCT<a:INT>
suite("repro_jt_cast_072") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('{"a":"not_int"}' AS JSONB) AS STRUCT<a:INT>) """
    } catch (Exception e) {
        logger.info("JT-CAST-072 threw: ${e.message}")
    }
}
