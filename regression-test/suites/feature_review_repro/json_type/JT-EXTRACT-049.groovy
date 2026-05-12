// JT-EXTRACT-049: extract_int 正向
suite("repro_jt_extract_049") {
    def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-049; observed=${r}")
}
