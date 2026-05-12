// JT-QUERY-028: contains_path one mode 任一存在
suite("repro_jt_query_028") {
    try {
        def r = sql """SELECT json_contains_path(CAST('{"a":1,"b":2}' AS JSONB), 'one', '\$.a', '\$.c')"""
        assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-QUERY-028; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-028: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
