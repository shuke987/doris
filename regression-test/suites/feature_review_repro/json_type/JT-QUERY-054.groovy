// JT-QUERY-054: json_valid 'null junk' — 应 0
suite("repro_jt_query_054") {
    def r = sql "SELECT json_valid('null junk')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "0" || v == "false",
        "JT-QUERY-054 (SEV): 'null junk' invalid → 0; observed=${r}")
}
