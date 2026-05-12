suite("repro_ct_cast_029") {
    def r = sql "SELECT CAST(array(1,2,3) AS ARRAY<STRING>)"
    String s = r[0][0].toString()
    assertTrue(s.contains("1"), "CT-CAST-029: INT->STRING; observed=${r}")
}
