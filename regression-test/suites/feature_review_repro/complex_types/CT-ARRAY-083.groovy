suite("repro_ct_array_083") {
    def r = sql "SELECT element_at(array(1,2,3), -1)"
    assertEquals(3, (r[0][0] as Number).intValue(), "CT-ARRAY-083: element_at(-1) tail; observed=${r}")
}
