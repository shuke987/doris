// JT-QUERY-067: json_unquote 非引号包裹
suite("repro_jt_query_067") {
    def r = sql """SELECT json_unquote('hi')"""
    assertEquals('hi', r[0][0]?.toString(), "JT-QUERY-067; observed=${r}")
}
