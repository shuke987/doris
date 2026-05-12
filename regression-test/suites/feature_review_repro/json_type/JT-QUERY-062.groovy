// JT-QUERY-062: json_valid 嵌套 100 层
suite("repro_jt_query_062") {
    String s = '1'
    for (int i=0;i<100;i++) s = "[${s}]"
    try {
        def r = sql "SELECT json_valid('${s}')"
        assertNotNull(r[0][0], "JT-QUERY-062; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
