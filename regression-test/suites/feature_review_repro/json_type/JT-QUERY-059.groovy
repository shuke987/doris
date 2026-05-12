// JT-QUERY-059: json_valid 合法
suite("repro_jt_query_059") {
    def r = sql """SELECT json_valid('{"a":1}')"""
    assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-QUERY-059; observed=${r}")
}
