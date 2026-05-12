// JT-QUERY-043: json_unquote 含中文
suite("repro_jt_query_043") {
    def r = sql "SELECT json_unquote('\"中文\"')"
    assertEquals("中文", r[0][0].toString(), "JT-QUERY-043; observed=${r}")
}
