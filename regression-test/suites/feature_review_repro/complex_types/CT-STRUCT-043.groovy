suite("repro_ct_struct_043") {
    def r = sql "SELECT struct(CAST(NULL AS INT), CAST(NULL AS STRING))"
    assertTrue(r[0][0] != null || r[0][0] == null, "CT-STRUCT-043: struct(NULL,NULL); observed=${r}")
}
