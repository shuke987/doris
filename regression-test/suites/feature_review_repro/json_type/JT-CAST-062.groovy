// JT-CAST-062: json_object('k', 1) implicit cast int → JSONB
suite("repro_jt_cast_062") {
    def r = sql "SELECT json_object('k', 1)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"k\":1"), "JT-CAST-062; observed=${r}")
}
