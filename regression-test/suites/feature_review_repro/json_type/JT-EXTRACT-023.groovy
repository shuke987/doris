// JT-EXTRACT-023: path 含 emoji key
suite("repro_jt_extract_023") {
    def r = sql "SELECT jsonb_extract(CAST('{\"🎉\":1}' AS JSONB), '\$.🎉')"
    String v = r[0][0]?.toString() ?: ""
    assertEquals("1", v, "JT-EXTRACT-023; observed=${r}")
}
