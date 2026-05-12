// JT-PARSE-049: int8 边界 128 (over int8)
suite("repro_jt_parse_049") {
    def r = sql "SELECT jsonb_type(jsonb_parse('128'), '\$')"
    // observed: "int" (no granular distinction)
    assertEquals("int", r[0][0].toString().toLowerCase(),
        "JT-PARSE-049; observed=${r}")
}
