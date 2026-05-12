suite("repro_ct_array_156") {
    def r = sql "SELECT array_sort(array(3,1,2))"
    assertEquals("[1, 2, 3]", r[0][0].toString(), "CT-ARRAY-156: array_sort; observed=${r}")
}
