suite("repro_ct_cast_028") {
    def r = sql "SELECT CAST(array(1,2,3) AS ARRAY<BIGINT>)"
    String s = r[0][0].toString()
    assertTrue(s.contains("1") && s.contains("3"), "CT-CAST-028: INT->BIGINT; observed=${r}")
}
