// JT-CROSS-021: JSONB × subquery
suite("repro_jt_cross_021") {
    def r = sql """
        SELECT jsonb_extract_int(j, '\$.a') FROM (SELECT CAST('{\"a\":42}' AS JSONB) AS j) t
    """
    assertEquals("42", r[0][0].toString(), "JT-CROSS-021; observed=${r}")
}
