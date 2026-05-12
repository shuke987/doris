suite("repro_ct_array_185") {
    def r = sql "SELECT array_pushback(array(), 1)"
    assertEquals("[1]", r[0][0].toString(), "CT-ARRAY-185: pushback empty; observed=${r}")
}
