// JT-QUERY-053: json_valid 'not' — 应 0，实际 cluster 返 1 = SEV
suite("repro_jt_query_053") {
    def r = sql "SELECT json_valid('not')"
    String v = r[0][0].toString().toLowerCase()
    // spec: 'not' is invalid JSON (only 'null'/'true'/'false' are bare literals)
    assertTrue(v == "0" || v == "false",
        "JT-QUERY-053 (SEV): 'not' invalid JSON should be 0; observed=${r}")
}
