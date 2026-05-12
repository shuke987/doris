// JT-EXTRACT-018: NULL path → NULL
suite("repro_jt_extract_018") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), NULL)"
    assertEquals(null, r[0][0], "JT-EXTRACT-018; observed=${r}")
}
