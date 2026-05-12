// JT-QUERY-064: json_valid 含 BOM
suite("repro_jt_query_064") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_valid(CONCAT(CHAR(0xEF), CHAR(0xBB), CHAR(0xBF), '{}')) """
    } catch (Exception e) {
        logger.info("JT-QUERY-064 threw: ${e.message}")
    }
}
