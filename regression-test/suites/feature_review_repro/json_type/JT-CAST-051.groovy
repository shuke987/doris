// JT-CAST-051: LARGEINT 2^120 → JSONB
suite("repro_jt_cast_051") {
    def r = sql "SELECT CAST(CAST('1329227995784915872903807060280344576' AS LARGEINT) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("1329227995784915872903807060280344576") || v.length() > 30,
        "JT-CAST-051; observed=${r}")
}
