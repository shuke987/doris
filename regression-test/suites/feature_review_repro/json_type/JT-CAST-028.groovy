// JT-CAST-028: jsonb T_Double 3.7 → INT
suite("repro_jt_cast_028") {
    def r = sql "SELECT CAST(CAST('3.7' AS JSONB) AS INT)"
    // observed cluster: truncates to 3 (or 4 if rounding). lock behavior
    String v = r[0][0].toString()
    assertTrue(v == "3" || v == "4",
        "JT-CAST-028: double → int truncate/round; observed=${r}")
}
