// JT-CROSS-012: JSONB × CTE
suite("repro_jt_cross_012") {
    def r = sql """
        WITH cte AS (SELECT CAST('{\"a\":1}' AS JSONB) AS j)
        SELECT jsonb_extract_int(j, '\$.a') FROM cte
    """
    assertEquals("1", r[0][0].toString(), "JT-CROSS-012; observed=${r}")
}
