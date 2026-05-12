suite("repro_ct_array_158") {
    def r = sql "SELECT array_sort(array('B','a'))"
    String s = r[0][0].toString()
    assertTrue(s.contains("B") && s.contains("a"), "CT-ARRAY-158: sort string; observed=${r}")
}
