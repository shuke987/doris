suite("repro_ct_cast_030") {
    def r = sql "SELECT CAST(array(1,2) AS ARRAY<DECIMAL(10,2)>)"
    assertTrue(r[0][0] != null, "CT-CAST-030: INT->DECIMAL; observed=${r}")
}
