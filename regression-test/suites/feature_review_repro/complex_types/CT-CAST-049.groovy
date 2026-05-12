suite("repro_ct_cast_049") {
    def r = sql "SELECT CAST(struct(1,'a') AS STRING)"
    String s = r[0][0].toString()
    assertTrue(s.contains("1") && s.contains("a"), "CT-CAST-049: struct->STRING; observed=${r}")
}
