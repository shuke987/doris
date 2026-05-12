suite("repro_ct_cast_019") {
    def r = sql "SELECT CAST('null' AS MAP<STRING,INT>)"
    assertTrue(r[0][0] != null || r[0][0] == null, "CT-CAST-019: 'null' MAP; observed=${r}")
}
