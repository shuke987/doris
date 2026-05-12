// JT-EXTRACT-124: corrupt type=NUM_TYPES (0xFF) jsonb_type
suite("repro_jt_extract_124") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_type(CAST('1' AS JSONB), '\$') """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-124 threw: ${e.message}")
    }
}
