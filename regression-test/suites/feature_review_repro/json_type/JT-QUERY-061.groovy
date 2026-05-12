// JT-QUERY-061: json_valid NULL
suite("repro_jt_query_061") {
    def r = sql """SELECT json_valid(NULL)"""
    assertEquals(null, r[0][0], "JT-QUERY-061: expect NULL; observed=${r}")
}
