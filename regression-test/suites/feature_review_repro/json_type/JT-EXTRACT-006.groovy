// JT-EXTRACT-006: $** super wildcard (recursive)
suite("repro_jt_extract_006") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":{\"b\":1}}' AS JSONB), '\$**.b')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1"), "JT-EXTRACT-006; observed=${r}")
}
