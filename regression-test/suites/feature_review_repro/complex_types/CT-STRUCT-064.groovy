suite("repro_ct_struct_064") {
    def r = sql "SELECT struct_element(CAST(NULL AS STRUCT<a:INT>), 'a')"
    assertEquals(null, r[0][0], "CT-STRUCT-064: NULL struct; observed=${r}")
}
