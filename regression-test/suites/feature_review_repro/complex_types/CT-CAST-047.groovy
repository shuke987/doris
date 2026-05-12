suite("repro_ct_cast_047") {
    def r = sql "SELECT CAST(array(1,2,3) AS STRING)"
    String s = r[0][0].toString()
    assertTrue(s.contains("1") && s.contains("3"), "CT-CAST-047: array->STRING; observed=${r}")
}
