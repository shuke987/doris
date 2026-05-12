suite("repro_ct_cast_048") {
    def r = sql "SELECT CAST(map('a',1,'b',2) AS STRING)"
    String s = r[0][0].toString()
    assertTrue(s.contains("a") || s.contains("1"), "CT-CAST-048: map->STRING; observed=${r}")
}
