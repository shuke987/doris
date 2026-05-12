suite("repro_ct_array_104") {
    def r = sql "SELECT array_position(array(1,2,3), 99)"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-104: not found -> 0; observed=${r}")
}
