// JT-QUERY-060: json_valid 非法
suite("repro_jt_query_060") {
    def r = sql """SELECT json_valid('{a:1')"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-060; observed=${r}")
}
