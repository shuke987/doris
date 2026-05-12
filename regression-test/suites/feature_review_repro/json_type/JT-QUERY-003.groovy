// JT-QUERY-003: json_length scalar → 1 (MySQL contract)
suite("repro_jt_query_003") {
    def r = sql "SELECT json_length(CAST('42' AS JSONB))"
    // MySQL: scalar length = 1
    assertEquals("1", r[0][0].toString(), "JT-QUERY-003; observed=${r}")
}
