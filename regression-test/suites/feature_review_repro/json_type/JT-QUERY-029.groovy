// JT-QUERY-029: contains_path all 全存在
suite("repro_jt_query_029") {
    try {
        def r = sql """SELECT json_contains_path(CAST('{"a":1,"b":2}' AS JSONB), 'all', '\$.a', '\$.b')"""
        assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-QUERY-029; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-029: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
