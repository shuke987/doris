suite("repro_ct_cast_036") {
    def r = sql "SELECT CAST(map('a',1) AS MAP<STRING,BIGINT>)"
    assertTrue(r[0][0] != null, "CT-CAST-036: MAP INT->BIGINT; observed=${r}")
}
