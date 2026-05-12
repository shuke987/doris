suite("repro_ct_array_121") {
    def r = sql "SELECT array_contains(array('中文','日本語'), '中文')"
    assertEquals(true, (r[0][0] as Boolean), "CT-ARRAY-121: chinese unicode; observed=${r}")
}
