// JT-CAST-049: INT 42 → JSONB
suite("repro_jt_cast_049") {
    def r = sql "SELECT CAST(42 AS JSONB)"
    assertEquals("42", r[0][0].toString(), "JT-CAST-049; observed=${r}")
}
