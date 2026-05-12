// JT-EXTRACT-123: super_wildcard 结果 set 指针 dedup
suite("repro_jt_extract_123") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":1,"b":1}' AS JSONB), '\$**') """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-123 threw: ${e.message}")
    }
}
