suite("repro_ct_cast_014") {
    def r = sql "SELECT CAST('[ 1 , 2 ]' AS ARRAY<INT>)"
    Object obs = r[0][0]
    String s = obs == null ? "null" : obs.toString()
    assertTrue(s.contains("1") && s.contains("2"), "CT-CAST-014: whitespace tolerated; observed=${r}")
}
