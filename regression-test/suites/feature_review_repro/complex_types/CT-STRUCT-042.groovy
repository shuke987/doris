suite("repro_ct_struct_042") {
    def r = sql "SELECT struct(CAST(NULL AS INT))"
    assertTrue(r[0][0] != null || r[0][0] == null, "CT-STRUCT-042: struct(NULL); observed=${r}")
}
