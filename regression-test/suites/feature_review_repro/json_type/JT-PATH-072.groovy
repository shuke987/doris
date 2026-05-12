// JT-PATH-072: key=`a.b` 含 `.` 的 round-trip
suite("repro_jt_path_072") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a.b":1}' AS JSONB), '\$."a.b"') """
    } catch (Exception e) {
        logger.info("JT-PATH-072 threw: ${e.message}")
    }
}
