suite("repro_ct_array_107") {
    def r = sql "SELECT array_position(CAST(NULL AS ARRAY<INT>), 1)"
    assertEquals(null, r[0][0], "CT-ARRAY-107: array_position(NULL,1)=NULL; observed=${r}")
}
